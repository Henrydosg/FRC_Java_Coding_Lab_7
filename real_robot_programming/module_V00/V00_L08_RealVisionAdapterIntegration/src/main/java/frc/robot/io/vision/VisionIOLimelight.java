// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.vision;

import edu.wpi.first.math.MatBuilder;
import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.NetworkTableValue;
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.io.vision.VisionIO.VisionTargetInputs;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Reads the repository-owned Limelight NetworkTables schema into the frozen VisionIO contract.
 *
 * <p>This adapter deliberately keeps Limelight field names and frame semantics private. It
 * exposes the target pose as {@code cameraToTarget}, which is the target pose in the camera frame;
 * the estimator-side inverse remains outside this adapter.
 */
public final class VisionIOLimelight implements VisionIO {
  private static final String kLimelightTableName = "limelight";
  private static final String kTvTopicName = "tv";
  private static final String kTidTopicName = "tid";
  private static final String kHeartbeatTopicName = "hb";
  private static final String kTargetPoseCameraSpaceTopicName = "targetpose_cameraspace";
  private static final double kTargetVisibleValue = 1.0;
  private static final int kTargetPoseValueCount = 6;
  private static final int kMaximumStaleHeartbeatCycles = 2;

  private final NetworkTableEntry tvEntry;
  private final NetworkTableEntry tidEntry;
  private final NetworkTableEntry heartbeatEntry;
  private final NetworkTableEntry targetPoseCameraSpaceEntry;
  private final Runnable afterFirstTargetReadHook;

  private boolean heartbeatInitialized;
  private double lastHeartbeat = Double.NaN;
  private int staleHeartbeatCycles;
  private long targetRefreshMarker;
  private long previousHeartbeatChange;

  /** Creates an adapter for the default {@code /limelight} NetworkTables table. */
  public VisionIOLimelight() {
    this(NetworkTableInstance.getDefault().getTable(kLimelightTableName));
  }

  /**
   * Creates an adapter over one NetworkTables table.
   *
   * <p>This package-private seam is used by deterministic tests. NetworkTables remains internal to
   * this real IO implementation and does not enter the VisionIO or Observation contracts.
   *
   * @param table Limelight NetworkTables table
   */
  VisionIOLimelight(NetworkTable table) {
    this(table, () -> {});
  }

  /**
   * Creates an adapter with a package-private hook for deterministic coherence regression tests.
   *
   * @param table Limelight NetworkTables table
   * @param afterFirstTargetReadHook action invoked between the two target reads
   */
  VisionIOLimelight(NetworkTable table, Runnable afterFirstTargetReadHook) {
    NetworkTable requiredTable = Objects.requireNonNull(table, "table");
    tvEntry = requiredTable.getEntry(kTvTopicName);
    tidEntry = requiredTable.getEntry(kTidTopicName);
    heartbeatEntry = requiredTable.getEntry(kHeartbeatTopicName);
    targetPoseCameraSpaceEntry = requiredTable.getEntry(kTargetPoseCameraSpaceTopicName);
    this.afterFirstTargetReadHook =
        Objects.requireNonNull(afterFirstTargetReadHook, "afterFirstTargetReadHook");
    targetRefreshMarker = maximumTargetLastChange();
    previousHeartbeatChange = heartbeatEntry.getLastChange();
  }

  /**
   * Fully overwrites one VisionIO transport snapshot from the current Limelight values.
   *
   * <p>A finite nonnegative heartbeat is required before the source is reported as available and
   * connected. Heartbeat progression is the primary frame discriminator. Repeated heartbeat
   * values invalidate the current sample immediately and become unavailable after the bounded
   * {@value #kMaximumStaleHeartbeatCycles}-cycle stale allowance. A heartbeat reset or a
   * reconnect also requires a complete target-topic refresh before an old target can be accepted
   * again. The adapter reads the independent NetworkTables entries as one stable read interval and
   * rechecks the heartbeat after the target read; this is a practical coherence guard, not an
   * atomic NetworkTables transaction. The target tuple is read twice and rejected if the two reads
   * disagree. A target is accepted only when the target-pose change timestamp is newer than both
   * the previous target boundary and the previous heartbeat change timestamp. The current stable
   * visibility and tag-id values remain required structural fields; their values may legitimately
   * remain unchanged while the target pose is refreshed. This deliberately fails closed when
   * NetworkTables cannot prove that the target measurement refreshed for the current heartbeat
   * interval; polling frequency, a changed heartbeat, or a changed visibility/tag field alone is
   * not freshness evidence.
   *
   * <p>A target is accepted only when {@code tv == 1}, {@code tid} is a positive integer, and the
   * target pose is an exactly six-element finite double array. {@code tv == 0} is deliberately an
   * invalid acquisition sample rather than a valid target-bearing observation. Any missing,
   * malformed, unacceptable, stale, or nonfinite required value fails closed with no target
   * measurement.
   *
   * @param inputs mutable snapshot to overwrite
   */
  @Override
  public void updateInputs(VisionIOInputs inputs) {
    VisionIOInputs requiredInputs = Objects.requireNonNull(inputs, "inputs");
    requiredInputs.available = false;
    requiredInputs.connected = false;
    requiredInputs.sampleValid = false;
    requiredInputs.targets = List.of();

    HeartbeatSnapshot heartbeatBefore = readHeartbeatSnapshot();
    if (heartbeatBefore.value() == null) {
      markHeartbeatUnavailable();
      return;
    }

    boolean heartbeatProgressed =
        !heartbeatInitialized || heartbeatBefore.value().doubleValue() != lastHeartbeat;
    if (!heartbeatProgressed) {
      staleHeartbeatCycles++;
      // Target changes observed while the heartbeat is stalled are not recovery evidence.
      advanceTargetRefreshMarker(maximumTargetLastChange());
      if (staleHeartbeatCycles > kMaximumStaleHeartbeatCycles) {
        requiredInputs.available = false;
        requiredInputs.connected = false;
        return;
      }
      requiredInputs.available = true;
      requiredInputs.connected = true;
      return;
    }

    heartbeatInitialized = true;
    lastHeartbeat = heartbeatBefore.value();
    staleHeartbeatCycles = 0;
    requiredInputs.available = true;
    requiredInputs.connected = true;

    TargetSnapshot targetSnapshot = readTargetSnapshot();
    afterFirstTargetReadHook.run();
    HeartbeatSnapshot heartbeatBetweenTargetReads = readHeartbeatSnapshot();
    TargetSnapshot targetSnapshotAfter = readTargetSnapshot();
    HeartbeatSnapshot heartbeatAfter = readHeartbeatSnapshot();
    if (!sameHeartbeat(heartbeatBefore, heartbeatBetweenTargetReads)
        || !sameHeartbeat(heartbeatBefore, heartbeatAfter)
        || !sameTargetSnapshot(targetSnapshot, targetSnapshotAfter)) {
      advanceTargetRefreshMarker(
          Math.max(targetSnapshot.maximumLastChange(), targetSnapshotAfter.maximumLastChange()));
      return;
    }
    targetSnapshot = targetSnapshotAfter;
    long freshnessBoundary = Math.max(targetRefreshMarker, previousHeartbeatChange);
    boolean freshTargetPose = targetSnapshot.hasFreshTargetPoseAfter(freshnessBoundary);
    advanceTargetRefreshMarker(targetSnapshot.maximumLastChange());
    previousHeartbeatChange = heartbeatBefore.lastChange();
    if (!freshTargetPose) {
      return;
    }

    Double targetVisible = readNumeric(targetSnapshot.tv().value());
    if (targetVisible == null || !Double.isFinite(targetVisible)) {
      return;
    }
    if (targetVisible == 0.0) {
      return;
    }
    if (targetVisible != kTargetVisibleValue) {
      return;
    }

    Double tagIdValue = readNumeric(targetSnapshot.tid().value());
    Integer tagId = decodePositiveTagId(tagIdValue);
    if (tagId == null) {
      return;
    }

    NetworkTableValue targetPoseValue = targetSnapshot.pose().value();
    if (targetPoseValue == null
        || !targetPoseValue.isValid()
        || !targetPoseValue.isDoubleArray()) {
      return;
    }
    double[] targetPoseCameraSpace = targetPoseValue.getDoubleArray();
    if (targetPoseCameraSpace.length != kTargetPoseValueCount) {
      return;
    }

    try {
      Transform3d cameraToTarget = convertTargetPose(targetPoseCameraSpace);
      requiredInputs.targets = List.of(new VisionTargetInputs(tagId, cameraToTarget));
      requiredInputs.sampleValid = true;
    } catch (IllegalArgumentException ignored) {
      // Malformed camera data is an invalid acquisition cycle, not a fabricated measurement.
    }
  }

  /**
   * Converts one Limelight {@code targetpose_cameraspace} tuple to the frozen WPILib contract.
   *
   * <p>Limelight translation axes are right, down, forward. The locked WPILib basis conversion
   * is {@code (x, y, z) = (limelightZ, -limelightX, -limelightY)}. The provisional commissioning
   * rotation convention is {@code Rx(-a3) * Ry(+a4) * Rz(+a5)} using column-vector semantics.
   *
   * @param targetPoseCameraSpace six values {@code [x, y, z, a3, a4, a5]} with angles in degrees
   * @return the target-relative-to-camera WPILib transform
   */
  static Transform3d convertTargetPose(double[] targetPoseCameraSpace) {
    double[] values = Objects.requireNonNull(targetPoseCameraSpace, "targetPoseCameraSpace");
    if (values.length != kTargetPoseValueCount) {
      throw new IllegalArgumentException("targetpose_cameraspace must contain exactly six values");
    }
    for (double value : values) {
      if (!Double.isFinite(value)) {
        throw new IllegalArgumentException("targetpose_cameraspace values must be finite");
      }
    }

    Translation3d translation = new Translation3d(values[2], -values[0], -values[1]);
    Rotation3d rotation =
        rotationFromH1(
            Units.degreesToRadians(values[3]),
            Units.degreesToRadians(values[4]),
            Units.degreesToRadians(values[5]));
    return new Transform3d(translation, rotation);
  }

  private static Rotation3d rotationFromH1(double a3Radians, double a4Radians, double a5Radians) {
    double cosineA3 = Math.cos(-a3Radians);
    double sineA3 = Math.sin(-a3Radians);
    Matrix<N3, N3> rotationX =
        MatBuilder.fill(
            Nat.N3(),
            Nat.N3(),
            1.0,
            0.0,
            0.0,
            0.0,
            cosineA3,
            -sineA3,
            0.0,
            sineA3,
            cosineA3);

    double cosineA4 = Math.cos(a4Radians);
    double sineA4 = Math.sin(a4Radians);
    Matrix<N3, N3> rotationY =
        MatBuilder.fill(
            Nat.N3(),
            Nat.N3(),
            cosineA4,
            0.0,
            sineA4,
            0.0,
            1.0,
            0.0,
            -sineA4,
            0.0,
            cosineA4);

    double cosineA5 = Math.cos(a5Radians);
    double sineA5 = Math.sin(a5Radians);
    Matrix<N3, N3> rotationZ =
        MatBuilder.fill(
            Nat.N3(),
            Nat.N3(),
            cosineA5,
            -sineA5,
            0.0,
            sineA5,
            cosineA5,
            0.0,
            0.0,
            0.0,
            1.0);

    return new Rotation3d(rotationX.times(rotationY).times(rotationZ));
  }

  private HeartbeatSnapshot readHeartbeatSnapshot() {
    Double heartbeat = readNumeric(heartbeatEntry.getValue());
    if (heartbeat == null || !Double.isFinite(heartbeat) || heartbeat < 0.0) {
      return new HeartbeatSnapshot(null, heartbeatEntry.getLastChange());
    }
    return new HeartbeatSnapshot(heartbeat, heartbeatEntry.getLastChange());
  }

  private TargetSnapshot readTargetSnapshot() {
    return new TargetSnapshot(
        new EntrySnapshot(tvEntry.getValue(), tvEntry.getLastChange()),
        new EntrySnapshot(tidEntry.getValue(), tidEntry.getLastChange()),
        new EntrySnapshot(
            targetPoseCameraSpaceEntry.getValue(), targetPoseCameraSpaceEntry.getLastChange()));
  }

  private boolean sameHeartbeat(HeartbeatSnapshot before, HeartbeatSnapshot after) {
    return before.value() != null
        && after.value() != null
        && before.value().doubleValue() == after.value().doubleValue()
        && before.lastChange() == after.lastChange();
  }

  private static boolean sameTargetSnapshot(TargetSnapshot before, TargetSnapshot after) {
    return sameEntrySnapshot(before.tv(), after.tv())
        && sameEntrySnapshot(before.tid(), after.tid())
        && sameEntrySnapshot(before.pose(), after.pose());
  }

  private static boolean sameEntrySnapshot(EntrySnapshot before, EntrySnapshot after) {
    return before.lastChange() == after.lastChange()
        && !valueChanged(before.value(), after.value());
  }

  private void markHeartbeatUnavailable() {
    advanceTargetRefreshMarker(maximumTargetLastChange());
    heartbeatInitialized = false;
    lastHeartbeat = Double.NaN;
    staleHeartbeatCycles = 0;
    previousHeartbeatChange = heartbeatEntry.getLastChange();
  }

  private void advanceTargetRefreshMarker(long candidate) {
    targetRefreshMarker = Math.max(targetRefreshMarker, candidate);
  }

  private long maximumTargetLastChange() {
    return Math.max(
        tvEntry.getLastChange(),
        Math.max(tidEntry.getLastChange(), targetPoseCameraSpaceEntry.getLastChange()));
  }

  private static Double readNumeric(NetworkTableValue value) {
    if (value == null || !value.isValid()) {
      return null;
    }
    if (value.isDouble()) {
      return value.getDouble();
    }
    if (value.isInteger()) {
      return (double) value.getInteger();
    }
    return null;
  }

  private static Integer decodePositiveTagId(Double tagIdValue) {
    if (tagIdValue == null
        || !Double.isFinite(tagIdValue)
        || tagIdValue <= 0.0
        || tagIdValue > Integer.MAX_VALUE
        || tagIdValue != Math.rint(tagIdValue)) {
      return null;
    }
    return (int) tagIdValue.doubleValue();
  }

  private record EntrySnapshot(NetworkTableValue value, long lastChange) {}

  private record HeartbeatSnapshot(Double value, long lastChange) {}

  private record TargetSnapshot(EntrySnapshot tv, EntrySnapshot tid, EntrySnapshot pose) {
    private boolean hasFreshTargetPoseAfter(long marker) {
      // tv and tid may legitimately retain their values while the measurement pose refreshes.
      return pose.lastChange() > 0L && pose.lastChange() > marker;
    }

    private long maximumLastChange() {
      return Math.max(tv.lastChange(), Math.max(tid.lastChange(), pose.lastChange()));
    }
  }

  private static boolean valueChanged(NetworkTableValue before, NetworkTableValue after) {
    if (before == null || after == null || !before.isValid() || !after.isValid()) {
      return before != after;
    }
    if (before.isDouble() && after.isDouble()) {
      return Double.compare(before.getDouble(), after.getDouble()) != 0;
    }
    if (before.isInteger() && after.isInteger()) {
      return before.getInteger() != after.getInteger();
    }
    if (before.isDoubleArray() && after.isDoubleArray()) {
      return !Arrays.equals(before.getDoubleArray(), after.getDoubleArray());
    }
    return !before.equals(after);
  }
}
