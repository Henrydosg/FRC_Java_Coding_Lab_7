// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.io.vision;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.observation.vision.VisionObservation.State;
import frc.robot.subsystems.VisionSubsystem;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies the Limelight JSON adapter and inherited target-frame conversion. */
class VisionIOLimelightTest {
  private static final double kTolerance = 1.0e-9;
  private NetworkTableInstance networkTableInstance;
  private NetworkTable table;
  private VisionIOLimelight adapter;

  @BeforeEach
  void setUp() {
    networkTableInstance = NetworkTableInstance.create();
    networkTableInstance.startLocal();
    table = networkTableInstance.getTable("limelight");
    adapter = new VisionIOLimelight(table);
  }

  @AfterEach
  void tearDown() {
    networkTableInstance.close();
  }

  @Test
  void parsesOneCoherentFlatRootJsonResultAndConvertsTiming() {
    publishJson(1, 1_500_000, 12.0, 8.0, 16, new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0});

    VisionIOInputs inputs = update();

    assertTrue(inputs.available);
    assertTrue(inputs.connected);
    assertTrue(inputs.sampleValid);
    assertTrue(inputs.timingValid);
    assertEquals(1.5, inputs.receiveTimestampSeconds, kTolerance);
    assertEquals(0.020, inputs.totalLatencySeconds, kTolerance);
    assertEquals(List.of(16), inputs.targets.stream().map(target -> target.tagId()).toList());
    Transform3d target = inputs.targets.get(0).cameraToTarget();
    assertEquals(3.0, target.getX(), kTolerance);
    assertEquals(-1.0, target.getY(), kTolerance);
    assertEquals(-2.0, target.getZ(), kTolerance);
  }

  @Test
  void acceptsHardwareObservedFlatRootFieldFamiliesWithoutResultsWrapper() {
    table.getEntry("json").setString(
        "{\"Fiducial\":[{\"fID\":16,\"t6t_cs\":[0,0,1,0,0,0]}],"
            + "\"cl\":7.528,\"fidx\":430945,\"pID\":1,"
            + "\"pTYPE\":\"pipe_fiducial\",\"ta\":0.25,\"tl\":12.736,"
            + "\"ts_nt\":3978065628,\"v\":1}");

    VisionIOInputs inputs = update();

    assertTrue(inputs.available);
    assertTrue(inputs.connected);
    assertTrue(inputs.sampleValid);
    assertTrue(inputs.timingValid);
    assertEquals(3_978.065628, inputs.receiveTimestampSeconds, kTolerance);
    assertEquals(0.020264, inputs.totalLatencySeconds, kTolerance);
    assertEquals(16, inputs.targets.get(0).tagId());
    assertEquals(1.0, inputs.targets.get(0).cameraToTarget().getX(), kTolerance);
  }

  @Test
  void selectsFiducialIdAndGeometryFromTheSameResult() {
    publishJsonWithFiducials(
        4,
        2_000_000,
        0.0,
        0.0,
        "[{\"fID\":7,\"t6t_cs\":[0.1,0.2,0.3,0,0,0]},"
            + "{\"fID\":16,\"t6t_cs\":[9,9,9,0,0,0]}]");

    VisionIOInputs inputs = update();

    assertTrue(inputs.sampleValid);
    assertEquals(7, inputs.targets.get(0).tagId());
    assertEquals(0.3, inputs.targets.get(0).cameraToTarget().getX(), kTolerance);
  }

  @Test
  void parseableFlatRootWithInvalidSampleRemainsConnectedAndMapsToInvalidSample() {
    table.getEntry("json").setString("{\"v\":1,\"fidx\":1}");

    VisionIOInputs inputs = update();

    assertTrue(inputs.available);
    assertTrue(inputs.connected);
    assertFalse(inputs.sampleValid);
    VisionSubsystem subsystem = new VisionSubsystem(adapter);
    subsystem.periodic();
    assertEquals(State.INVALID_SAMPLE, subsystem.getObservation().state());
  }

  @Test
  void rejectsDuplicateAndDecreasingFrameIndexes() {
    publishJson(10, 2_000_000, 1.0, 0.0, 16, new double[] {0, 0, 1, 0, 0, 0});
    assertTrue(update().sampleValid);

    publishJson(10, 2_100_000, 1.0, 0.0, 16, new double[] {0, 0, 1, 0, 0, 0});
    assertFalse(update().sampleValid);
    assertFalse(update().timingValid);

    publishJson(9, 2_200_000, 1.0, 0.0, 16, new double[] {0, 0, 1, 0, 0, 0});
    assertFalse(update().sampleValid);
  }

  @Test
  void rejectsMalformedAndMissingRequiredData() {
    table.getEntry("json").setString("");
    VisionIOInputs blank = update();
    assertFalse(blank.available);
    assertFalse(blank.connected);

    table.getEntry("json").setString("{not-json");
    VisionIOInputs malformed = update();
    assertFalse(malformed.available);
    assertFalse(malformed.connected);

    table.getEntry("json").setString("[]");
    VisionIOInputs nonObject = update();
    assertFalse(nonObject.available);
    assertFalse(nonObject.connected);

    publishJsonWithFiducials(1, 2_000_000, 1.0, 0.0, "[]");
    VisionIOInputs missingTarget = update();
    assertTrue(missingTarget.available);
    assertTrue(missingTarget.connected);
    assertFalse(missingTarget.sampleValid);

    table.getEntry("json").setString(
        "{\"v\":1,\"ts_nt\":0,\"cl\":0,\"tl\":0,"
            + "\"fidx\":2,\"Fiducial\":[{\"fID\":16,"
            + "\"t6t_cs\":[0,0,1,0,0,0]}]}");
    assertFalse(update().sampleValid);
  }

  @Test
  void rejectsNegativeLatencyAndNonfiniteGeometry() {
    publishJson(1, 2_000_000, -1.0, 0.0, 16, new double[] {0, 0, 1, 0, 0, 0});
    assertFalse(update().sampleValid);

    publishJson(2, 2_100_000, 0.0, 0.0, 16, new double[] {0, 0, 1, 0, 0, 0});
    assertTrue(update().sampleValid);

    table.getEntry("json").setString(
        "{\"v\":1,\"ts_nt\":2200000,\"cl\":0,\"tl\":0,"
            + "\"fidx\":3,\"Fiducial\":[{\"fID\":16,"
            + "\"t6t_cs\":[0,0,\"NaN\",0,0,0]}]}");
    assertFalse(update().sampleValid);
  }

  @Test
  void reconnectRequiresChangedJsonAfterTheDisconnectBoundary() {
    publishJson(1, 2_000_000, 0.0, 0.0, 16, new double[] {0, 0, 1, 0, 0, 0});
    assertTrue(update().sampleValid);

    table.getEntry("json").setString("");
    assertFalse(update().connected);

    VisionIOInputs cached = update();
    assertFalse(cached.connected);
    assertFalse(cached.sampleValid);

    publishJson(1, 2_000_000, 0.0, 0.0, 16, new double[] {0, 0, 1, 0, 0, 0});
    assertTrue(update().sampleValid);
  }

  @Test
  void inheritedTranslationAndRotationContractRemainsStable() {
    Transform3d actual =
        VisionIOLimelight.convertTargetPose(new double[] {1.0, 2.0, 3.0, 90.0, 0.0, 0.0});

    assertEquals(3.0, actual.getX(), kTolerance);
    assertEquals(-1.0, actual.getY(), kTolerance);
    assertEquals(-2.0, actual.getZ(), kTolerance);
    assertEquals(-Math.PI / 2.0, actual.getRotation().getX(), kTolerance);
    assertEquals(0.0, actual.getRotation().getY(), kTolerance);
    assertEquals(0.0, actual.getRotation().getZ(), kTolerance);
    assertEquals(0.0, new Rotation3d().getAngle(), kTolerance);
  }

  private VisionIOInputs update() {
    VisionIOInputs inputs = new VisionIOInputs();
    adapter.updateInputs(inputs);
    return inputs;
  }

  private void publishJson(
      long fidx,
      double tsNt,
      double cl,
      double tl,
      int tagId,
      double[] pose) {
    publishJsonWithFiducials(
        fidx,
        tsNt,
        cl,
        tl,
        "[{\"fID\":" + tagId + ",\"t6t_cs\":["
            + pose[0] + "," + pose[1] + "," + pose[2] + "," + pose[3] + "," + pose[4] + ","
            + pose[5] + "]}]");
  }

  private void publishJsonWithFiducials(
      long fidx,
      double tsNt,
      double cl,
      double tl,
      String fiducials) {
    table.getEntry("json").setString(
        "{\"v\":1,\"ts_nt\":" + tsNt
            + ",\"cl\":" + cl
            + ",\"tl\":" + tl
            + ",\"fidx\":" + fidx
            + ",\"Fiducial\":" + fiducials + "}");
  }
}
