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
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.io.vision.VisionIO.VisionTargetInputs;
import java.util.List;
import java.util.Objects;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Reads one coherent Limelight JSON result into the vendor-neutral VisionIO contract.
 *
 * <p>Limelight field names, result timing fields, and the private frame index remain inside this
 * real adapter. The target geometry is converted to the inherited camera-to-target WPILib
 * transform before leaving the IO boundary.
 */
public final class VisionIOLimelight implements VisionIO {
  private static final String kLimelightTableName = "limelight";
  private static final String kJsonTopicName = "json";
  private static final String kValidFieldName = "v";
  private static final String kTimestampNtFieldName = "ts_nt";
  private static final String kCaptureLatencyFieldName = "cl";
  private static final String kPipelineLatencyFieldName = "tl";
  private static final String kFrameIndexFieldName = "fidx";
  private static final String kFiducialsFieldName = "Fiducial";
  private static final String kFiducialIdFieldName = "fID";
  private static final String kTargetPoseCameraSpaceFieldName = "t6t_cs";
  private static final double kValidResultValue = 1.0;
  private static final int kTargetPoseValueCount = 6;

  private final NetworkTableEntry jsonEntry;
  private long sourceChangeBoundary;
  private boolean sourceSessionConnected;
  private Long lastFrameIndex;

  /** Creates an adapter for the default {@code /limelight/json} NetworkTables entry. */
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
    jsonEntry = Objects.requireNonNull(table, "table").getEntry(kJsonTopicName);
    sourceChangeBoundary = jsonEntry.getLastChange();
  }

  /**
   * Replaces all transport fields from one coherent Limelight JSON result.
   *
   * <p>The source session is deliberately conservative. A missing or malformed JSON value closes
   * the current session; a later session must publish a newly changed result before a target can be
   * accepted. The private {@code fidx} must strictly progress and has no invented wrap semantics.
   *
   * @param inputs mutable snapshot to overwrite
   */
  @Override
  public void updateInputs(VisionIOInputs inputs) {
    VisionIOInputs requiredInputs = Objects.requireNonNull(inputs, "inputs");
    resetInputs(requiredInputs);

    long jsonChange = jsonEntry.getLastChange();
    String json = jsonEntry.getString("");
    if (json == null || json.isBlank()) {
      closeSourceSession(jsonChange);
      return;
    }

    JSONObject results;
    try {
      results = readResults(json);
    } catch (IllegalArgumentException | ParseException failure) {
      closeSourceSession(jsonChange);
      return;
    }

    requiredInputs.available = true;
    requiredInputs.connected = true;
    if (!sourceSessionConnected) {
      if (jsonChange <= sourceChangeBoundary) {
        return;
      }
      sourceSessionConnected = true;
      lastFrameIndex = null;
    }

    Long frameIndex = decodeFrameIndex(readNumber(results, kFrameIndexFieldName));
    if (frameIndex == null) {
      return;
    }
    if (lastFrameIndex != null && frameIndex <= lastFrameIndex) {
      return;
    }
    lastFrameIndex = frameIndex;

    Double validValue = readNumber(results, kValidFieldName);
    if (validValue == null || validValue.doubleValue() != kValidResultValue) {
      return;
    }

    Double timestampNtMicros = readNumber(results, kTimestampNtFieldName);
    Double captureLatencyMillis = readNumber(results, kCaptureLatencyFieldName);
    Double pipelineLatencyMillis = readNumber(results, kPipelineLatencyFieldName);
    if (timestampNtMicros == null
        || !Double.isFinite(timestampNtMicros)
        || timestampNtMicros <= 0.0
        || captureLatencyMillis == null
        || !Double.isFinite(captureLatencyMillis)
        || captureLatencyMillis < 0.0
        || pipelineLatencyMillis == null
        || !Double.isFinite(pipelineLatencyMillis)
        || pipelineLatencyMillis < 0.0) {
      return;
    }

    double receiveTimestampSeconds = timestampNtMicros / 1_000_000.0;
    double totalLatencySeconds =
        (captureLatencyMillis + pipelineLatencyMillis) / 1_000.0;
    if (!Double.isFinite(receiveTimestampSeconds)
        || receiveTimestampSeconds <= 0.0
        || !Double.isFinite(totalLatencySeconds)
        || totalLatencySeconds < 0.0) {
      return;
    }

    JSONArray fiducials = readArray(results, kFiducialsFieldName);
    if (fiducials == null || fiducials.isEmpty()) {
      return;
    }

    JSONObject selectedFiducial = readObject(fiducials.get(0));
    Integer tagId = decodePositiveTagId(readNumber(selectedFiducial, kFiducialIdFieldName));
    double[] targetPoseCameraSpace =
        readSixElementFiniteArray(selectedFiducial, kTargetPoseCameraSpaceFieldName);
    if (tagId == null || targetPoseCameraSpace == null) {
      return;
    }

    try {
      Transform3d cameraToTarget = convertTargetPose(targetPoseCameraSpace);
      requiredInputs.timingValid = true;
      requiredInputs.receiveTimestampSeconds = receiveTimestampSeconds;
      requiredInputs.totalLatencySeconds = totalLatencySeconds;
      requiredInputs.targets = List.of(new VisionTargetInputs(tagId, cameraToTarget));
      requiredInputs.sampleValid = true;
    } catch (IllegalArgumentException ignored) {
      // Malformed geometry is an invalid acquisition cycle, not a fabricated measurement.
    }
  }

  /**
   * Converts one Limelight {@code t6t_cs} tuple to the frozen WPILib camera-to-target contract.
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
      throw new IllegalArgumentException("t6t_cs must contain exactly six values");
    }
    for (double value : values) {
      if (!Double.isFinite(value)) {
        throw new IllegalArgumentException("t6t_cs values must be finite");
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

  private static JSONObject readResults(String json) throws ParseException {
    Object parsed = new JSONParser().parse(json);
    if (!(parsed instanceof JSONObject root)) {
      throw new IllegalArgumentException("Limelight JSON root must be an object");
    }
    return root;
  }

  private static JSONObject readObject(Object value) {
    if (!(value instanceof JSONObject object)) {
      throw new IllegalArgumentException("Limelight JSON field must be an object");
    }
    return object;
  }

  private static JSONArray readArray(JSONObject object, String name) {
    Object value = object.get(name);
    return value instanceof JSONArray array ? array : null;
  }

  private static Double readNumber(JSONObject object, String name) {
    Object value = object.get(name);
    return value instanceof Number number ? number.doubleValue() : null;
  }

  private static double[] readSixElementFiniteArray(JSONObject object, String name) {
    Object value = object.get(name);
    if (!(value instanceof JSONArray array) || array.size() != kTargetPoseValueCount) {
      return null;
    }

    double[] result = new double[kTargetPoseValueCount];
    for (int index = 0; index < result.length; index++) {
      Object element = array.get(index);
      if (!(element instanceof Number number)) {
        return null;
      }
      result[index] = number.doubleValue();
      if (!Double.isFinite(result[index])) {
        return null;
      }
    }
    return result;
  }

  private static Long decodeFrameIndex(Double value) {
    if (value == null
        || !Double.isFinite(value)
        || value < 0.0
        || value != Math.rint(value)
        || value > Long.MAX_VALUE) {
      return null;
    }
    return value.longValue();
  }

  private static Integer decodePositiveTagId(Double value) {
    if (value == null
        || !Double.isFinite(value)
        || value <= 0.0
        || value > Integer.MAX_VALUE
        || value != Math.rint(value)) {
      return null;
    }
    return value.intValue();
  }

  private void closeSourceSession(long currentChange) {
    sourceSessionConnected = false;
    lastFrameIndex = null;
    sourceChangeBoundary = Math.max(sourceChangeBoundary, currentChange);
  }

  private static void resetInputs(VisionIOInputs inputs) {
    inputs.available = false;
    inputs.connected = false;
    inputs.sampleValid = false;
    inputs.timingValid = false;
    inputs.receiveTimestampSeconds = 0.0;
    inputs.totalLatencySeconds = 0.0;
    inputs.targets = List.of();
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
}
