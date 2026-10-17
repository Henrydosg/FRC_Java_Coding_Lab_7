// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.vision;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Quaternion;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.io.vision.VisionIO.VisionTargetInputs;
import frc.robot.vision.AprilTagFieldLayoutContract;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Provides deterministic, vendor-neutral VisionIO simulation from explicit field truth.
 *
 * <p>The simulator generates target-relative camera transforms from an explicitly selected frame.
 * It never estimates robot pose from a target and never advances its frame implicitly. Timing is
 * also supplied by the explicit frame rather than by a clock.
 */
public final class VisionIOSim implements VisionIO {
  private final AprilTagFieldLayoutContract fieldLayout;
  private final Transform3d robotToCamera;
  private FrameSnapshot currentFrame = FrameSnapshot.unavailable();

  /**
   * Creates a deterministic simulator with one official field reference and fixed camera mounting
   * transform.
   *
   * @param fieldLayout official canonical field-to-tag reference contract
   * @param robotToCamera fixed transform from robot frame to camera frame
   * @throws NullPointerException when either argument is null
   * @throws IllegalArgumentException when the camera transform is nonfinite or invalid
   */
  public VisionIOSim(
      AprilTagFieldLayoutContract fieldLayout,
      Transform3d robotToCamera) {
    this.fieldLayout = Objects.requireNonNull(fieldLayout, "fieldLayout");
    this.robotToCamera = copyFiniteTransform(robotToCamera, "robotToCamera");
  }

  /**
   * Replaces the explicit scenario frame after validating and synthesizing its complete output.
   *
   * <p>Validation and geometry synthesis occur before the current frame is replaced. A rejected
   * frame therefore cannot partially change the simulator's previous output.
   *
   * @param frame explicit simulation scenario
   * @throws NullPointerException when {@code frame} is null
   * @throws IllegalArgumentException when a target ID is unknown or generated geometry is invalid
   */
  public void setFrame(Frame frame) {
    Frame requiredFrame = Objects.requireNonNull(frame, "frame");
    currentFrame = buildSnapshot(requiredFrame);
  }

  /**
   * Overwrites every transport field from the current explicit frame.
   *
   * @param inputs mutable transport snapshot to overwrite
   * @throws NullPointerException when {@code inputs} is null
   */
  @Override
  public void updateInputs(VisionIOInputs inputs) {
    VisionIOInputs requiredInputs = Objects.requireNonNull(inputs, "inputs");
    requiredInputs.available = currentFrame.available;
    requiredInputs.connected = currentFrame.connected;
    requiredInputs.sampleValid = currentFrame.sampleValid;
    requiredInputs.timingValid = currentFrame.timingValid;
    requiredInputs.receiveTimestampSeconds = currentFrame.receiveTimestampSeconds;
    requiredInputs.totalLatencySeconds = currentFrame.totalLatencySeconds;
    requiredInputs.targets = currentFrame.targets;
  }

  private FrameSnapshot buildSnapshot(Frame frame) {
    return switch (frame.state) {
      case UNAVAILABLE -> FrameSnapshot.unavailable();
      case DISCONNECTED -> FrameSnapshot.disconnected();
      case INVALID_SAMPLE -> FrameSnapshot.invalidSample();
      case NO_TARGETS -> FrameSnapshot.noTargets();
      case TARGETS_PRESENT -> buildTargetsPresentSnapshot(frame);
    };
  }

  private FrameSnapshot buildTargetsPresentSnapshot(Frame frame) {
    Pose3d fieldToRobotGroundTruth =
        frame.fieldToRobotGroundTruth.orElseThrow(
            () -> new IllegalStateException("targetsPresent frame requires robot ground truth"));
    Pose3d fieldToCamera =
        copyFinitePose(
            fieldToRobotGroundTruth.transformBy(robotToCamera), "generated fieldToCamera");

    List<VisionTargetInputs> targets = new ArrayList<>(frame.visibleTagIds.size());
    for (int tagId : frame.visibleTagIds) {
      Pose3d fieldToTag =
          fieldLayout
              .getTagPose(tagId)
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "visible tag ID is not present in the selected field: " + tagId));
      Transform3d cameraToTarget =
          copyFiniteTransform(
              new Transform3d(fieldToCamera, fieldToTag),
              "generated cameraToTarget for tag " + tagId);
      targets.add(new VisionTargetInputs(tagId, cameraToTarget));
    }

    return new FrameSnapshot(
        true,
        true,
        true,
        frame.timingValid,
        frame.receiveTimestampSeconds,
        frame.totalLatencySeconds,
        List.copyOf(targets));
  }

  /**
   * Describes one explicit deterministic simulator scenario.
   *
   * <p>The scenario factories are the only public way to create a frame. The two-argument
   * {@link #targetsPresent(Pose3d, List)} factory preserves the inherited geometry-only fixture;
   * the four-argument overload supplies explicit timing for L09 qualification tests.
   */
  public static final class Frame {
    private final State state;
    private final Optional<Pose3d> fieldToRobotGroundTruth;
    private final List<Integer> visibleTagIds;
    private final boolean timingValid;
    private final double receiveTimestampSeconds;
    private final double totalLatencySeconds;

    private Frame(
        State state,
        Optional<Pose3d> fieldToRobotGroundTruth,
        List<Integer> visibleTagIds) {
      this(state, fieldToRobotGroundTruth, visibleTagIds, false, 0.0, 0.0);
    }

    private Frame(
        State state,
        Optional<Pose3d> fieldToRobotGroundTruth,
        List<Integer> visibleTagIds,
        boolean timingValid,
        double receiveTimestampSeconds,
        double totalLatencySeconds) {
      this.state = Objects.requireNonNull(state, "state");
      this.fieldToRobotGroundTruth =
          Objects.requireNonNull(fieldToRobotGroundTruth, "fieldToRobotGroundTruth");
      this.visibleTagIds = List.copyOf(Objects.requireNonNull(visibleTagIds, "visibleTagIds"));
      if (!Double.isFinite(receiveTimestampSeconds)
          || !Double.isFinite(totalLatencySeconds)
          || totalLatencySeconds < 0.0) {
        throw new IllegalArgumentException("frame timing must be finite and nonnegative");
      }
      this.timingValid = timingValid;
      this.receiveTimestampSeconds = receiveTimestampSeconds;
      this.totalLatencySeconds = totalLatencySeconds;
    }

    /** @return an unavailable source with no target observations */
    public static Frame unavailable() {
      return new Frame(State.UNAVAILABLE, Optional.empty(), List.of());
    }

    /** @return a disconnected source with no target observations */
    public static Frame disconnected() {
      return new Frame(State.DISCONNECTED, Optional.empty(), List.of());
    }

    /** @return an available and connected source with an invalid sample and no targets */
    public static Frame invalidSample() {
      return new Frame(State.INVALID_SAMPLE, Optional.empty(), List.of());
    }

    /** @return an available, connected, valid sample with no visible targets */
    public static Frame noTargets() {
      return new Frame(State.NO_TARGETS, Optional.empty(), List.of());
    }

    /**
     * Creates an inherited geometry-only target-bearing frame.
     *
     * @param fieldToRobotGroundTruth robot pose in the canonical field frame
     * @param visibleTagIds positive, distinct IDs in desired acquisition order
     * @return a defensive, immutable target-bearing scenario
     */
    public static Frame targetsPresent(
        Pose3d fieldToRobotGroundTruth,
        List<Integer> visibleTagIds) {
      return targetsPresent(fieldToRobotGroundTruth, visibleTagIds, 0.0, 0.0, false);
    }

    /**
     * Creates a valid target-bearing frame with explicit deterministic timing facts.
     *
     * @param fieldToRobotGroundTruth robot pose in the canonical field frame
     * @param visibleTagIds positive, distinct IDs in desired acquisition order
     * @param receiveTimestampSeconds explicit receive timestamp in seconds
     * @param totalLatencySeconds explicit nonnegative latency in seconds
     * @return a target-bearing frame with valid timing
     */
    public static Frame targetsPresent(
        Pose3d fieldToRobotGroundTruth,
        List<Integer> visibleTagIds,
        double receiveTimestampSeconds,
        double totalLatencySeconds) {
      return targetsPresent(
          fieldToRobotGroundTruth,
          visibleTagIds,
          receiveTimestampSeconds,
          totalLatencySeconds,
          true);
    }

    private static Frame targetsPresent(
        Pose3d fieldToRobotGroundTruth,
        List<Integer> visibleTagIds,
        double receiveTimestampSeconds,
        double totalLatencySeconds,
        boolean timingValid) {
      Pose3d ownedGroundTruth =
          copyFinitePose(
              Objects.requireNonNull(fieldToRobotGroundTruth, "fieldToRobotGroundTruth"),
              "fieldToRobotGroundTruth");
      List<Integer> ownedTagIds =
          List.copyOf(Objects.requireNonNull(visibleTagIds, "visibleTagIds"));
      if (ownedTagIds.isEmpty()) {
        throw new IllegalArgumentException("visibleTagIds must not be empty");
      }

      Set<Integer> distinctTagIds = new HashSet<>();
      for (Integer tagId : ownedTagIds) {
        if (tagId == null || tagId <= 0) {
          throw new IllegalArgumentException("visible tag IDs must be positive: " + tagId);
        }
        if (!distinctTagIds.add(tagId)) {
          throw new IllegalArgumentException("visible tag IDs must be distinct: " + tagId);
        }
      }
      if (!Double.isFinite(receiveTimestampSeconds)
          || !Double.isFinite(totalLatencySeconds)
          || totalLatencySeconds < 0.0) {
        throw new IllegalArgumentException("simulation timing must be finite and nonnegative");
      }

      return new Frame(
          State.TARGETS_PRESENT,
          Optional.of(ownedGroundTruth),
          ownedTagIds,
          timingValid,
          receiveTimestampSeconds,
          totalLatencySeconds);
    }
  }

  private enum State {
    UNAVAILABLE,
    DISCONNECTED,
    INVALID_SAMPLE,
    NO_TARGETS,
    TARGETS_PRESENT
  }

  private static final class FrameSnapshot {
    private final boolean available;
    private final boolean connected;
    private final boolean sampleValid;
    private final boolean timingValid;
    private final double receiveTimestampSeconds;
    private final double totalLatencySeconds;
    private final List<VisionTargetInputs> targets;

    private FrameSnapshot(
        boolean available,
        boolean connected,
        boolean sampleValid,
        boolean timingValid,
        double receiveTimestampSeconds,
        double totalLatencySeconds,
        List<VisionTargetInputs> targets) {
      this.available = available;
      this.connected = connected;
      this.sampleValid = sampleValid;
      this.timingValid = timingValid;
      this.receiveTimestampSeconds = receiveTimestampSeconds;
      this.totalLatencySeconds = totalLatencySeconds;
      this.targets = List.copyOf(Objects.requireNonNull(targets, "targets"));
    }

    private static FrameSnapshot unavailable() {
      return new FrameSnapshot(false, false, false, false, 0.0, 0.0, List.of());
    }

    private static FrameSnapshot disconnected() {
      return new FrameSnapshot(true, false, false, false, 0.0, 0.0, List.of());
    }

    private static FrameSnapshot invalidSample() {
      return new FrameSnapshot(true, true, false, false, 0.0, 0.0, List.of());
    }

    private static FrameSnapshot noTargets() {
      return new FrameSnapshot(true, true, true, false, 0.0, 0.0, List.of());
    }
  }

  private static Pose3d copyFinitePose(Pose3d pose, String name) {
    Pose3d requiredPose = Objects.requireNonNull(pose, name);
    Translation3d translation =
        Objects.requireNonNull(requiredPose.getTranslation(), name + ".translation");
    Rotation3d rotation = Objects.requireNonNull(requiredPose.getRotation(), name + ".rotation");
    Quaternion quaternion =
        Objects.requireNonNull(rotation.getQuaternion(), name + ".rotation.quaternion");
    requireFinite(translation.getX(), name + ".translation.x");
    requireFinite(translation.getY(), name + ".translation.y");
    requireFinite(translation.getZ(), name + ".translation.z");
    requireFinite(quaternion.getW(), name + ".rotation.w");
    requireFinite(quaternion.getX(), name + ".rotation.x");
    requireFinite(quaternion.getY(), name + ".rotation.y");
    requireFinite(quaternion.getZ(), name + ".rotation.z");
    requireNonzeroQuaternionNorm(quaternion, name + ".rotation");

    return new Pose3d(
        new Translation3d(translation.getX(), translation.getY(), translation.getZ()),
        new Rotation3d(
            new Quaternion(
                quaternion.getW(), quaternion.getX(), quaternion.getY(), quaternion.getZ())));
  }

  private static Transform3d copyFiniteTransform(Transform3d transform, String name) {
    Transform3d requiredTransform = Objects.requireNonNull(transform, name);
    Translation3d translation =
        Objects.requireNonNull(requiredTransform.getTranslation(), name + ".translation");
    Rotation3d rotation = Objects.requireNonNull(requiredTransform.getRotation(), name + ".rotation");
    Quaternion quaternion =
        Objects.requireNonNull(rotation.getQuaternion(), name + ".rotation.quaternion");
    requireFinite(translation.getX(), name + ".translation.x");
    requireFinite(translation.getY(), name + ".translation.y");
    requireFinite(translation.getZ(), name + ".translation.z");
    requireFinite(quaternion.getW(), name + ".rotation.w");
    requireFinite(quaternion.getX(), name + ".rotation.x");
    requireFinite(quaternion.getY(), name + ".rotation.y");
    requireFinite(quaternion.getZ(), name + ".rotation.z");
    requireNonzeroQuaternionNorm(quaternion, name + ".rotation");

    return new Transform3d(
        new Translation3d(translation.getX(), translation.getY(), translation.getZ()),
        new Rotation3d(
            new Quaternion(
                quaternion.getW(), quaternion.getX(), quaternion.getY(), quaternion.getZ())));
  }

  private static void requireNonzeroQuaternionNorm(Quaternion quaternion, String name) {
    double normSquared =
        quaternion.getW() * quaternion.getW()
            + quaternion.getX() * quaternion.getX()
            + quaternion.getY() * quaternion.getY()
            + quaternion.getZ() * quaternion.getZ();
    if (!Double.isFinite(normSquared) || normSquared <= 0.0) {
      throw new IllegalArgumentException(name + " quaternion must have finite nonzero norm");
    }
  }

  private static void requireFinite(double value, String name) {
    if (!Double.isFinite(value)) {
      throw new IllegalArgumentException(name + " must be finite");
    }
  }
}

