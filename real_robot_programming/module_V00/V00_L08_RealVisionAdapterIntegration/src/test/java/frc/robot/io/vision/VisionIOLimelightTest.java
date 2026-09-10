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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.RobotBase;
import frc.robot.RobotContainer;
import frc.robot.io.vision.VisionIO.VisionIOInputs;
import frc.robot.io.vision.VisionIO.VisionTargetInputs;
import frc.robot.observation.vision.VisionObservation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Verifies the repository-owned Limelight adapter and its locked conversions. */
class VisionIOLimelightTest {
  private static final double kTolerance = 1.0e-9;
  private static final String kTableName = "limelight";
  private static final String kTv = "tv";
  private static final String kTid = "tid";
  private static final String kHeartbeat = "hb";
  private static final String kTargetPose = "targetpose_cameraspace";

  private NetworkTableInstance networkTableInstance;
  private NetworkTable table;
  private VisionIOLimelight adapter;

  @BeforeEach
  void setUp() {
    networkTableInstance = NetworkTableInstance.create();
    networkTableInstance.startLocal();
    table = networkTableInstance.getTable(kTableName);
    adapter = new VisionIOLimelight(table);
    table.getEntry(kHeartbeat).setInteger(1L);
  }

  @AfterEach
  void tearDown() {
    networkTableInstance.close();
  }

  @Test
  void limelightPositiveXRightMapsToWpilibNegativeY() {
    Transform3d actual = VisionIOLimelight.convertTargetPose(new double[] {1.0, 0.0, 0.0, 0.0, 0.0, 0.0});

    assertEquals(0.0, actual.getX(), kTolerance);
    assertEquals(-1.0, actual.getY(), kTolerance);
    assertEquals(0.0, actual.getZ(), kTolerance);
  }

  @Test
  void limelightPositiveYDownMapsToWpilibNegativeZ() {
    Transform3d actual = VisionIOLimelight.convertTargetPose(new double[] {0.0, 1.0, 0.0, 0.0, 0.0, 0.0});

    assertEquals(0.0, actual.getX(), kTolerance);
    assertEquals(0.0, actual.getY(), kTolerance);
    assertEquals(-1.0, actual.getZ(), kTolerance);
  }

  @Test
  void limelightPositiveZForwardMapsToWpilibPositiveX() {
    Transform3d actual = VisionIOLimelight.convertTargetPose(new double[] {0.0, 0.0, 1.0, 0.0, 0.0, 0.0});

    assertEquals(1.0, actual.getX(), kTolerance);
    assertEquals(0.0, actual.getY(), kTolerance);
    assertEquals(0.0, actual.getZ(), kTolerance);
  }

  @Test
  void combinedTranslationUsesTheLockedBasis() {
    Transform3d actual = VisionIOLimelight.convertTargetPose(new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});

    assertEquals(3.0, actual.getX(), kTolerance);
    assertEquals(-1.0, actual.getY(), kTolerance);
    assertEquals(-2.0, actual.getZ(), kTolerance);
  }

  @Test
  void zeroRotationIsIdentity() {
    Rotation3d rotation = VisionIOLimelight.convertTargetPose(new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 0.0}).getRotation();

    assertEquals(0.0, rotation.getAngle(), kTolerance);
  }

  @Test
  void positiveA3AppliesNegativeXRotation() {
    Rotation3d rotation = VisionIOLimelight.convertTargetPose(new double[] {0.0, 0.0, 0.0, 90.0, 0.0, 0.0}).getRotation();

    assertEquals(-Math.PI / 2.0, rotation.getX(), kTolerance);
  }

  @Test
  void positiveA4AppliesPositiveYRotation() {
    Rotation3d rotation = VisionIOLimelight.convertTargetPose(new double[] {0.0, 0.0, 0.0, 0.0, 90.0, 0.0}).getRotation();

    assertEquals(Math.PI / 2.0, rotation.getY(), kTolerance);
  }

  @Test
  void positiveA5AppliesPositiveZRotation() {
    Rotation3d rotation = VisionIOLimelight.convertTargetPose(new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 90.0}).getRotation();

    assertEquals(Math.PI / 2.0, rotation.getZ(), kTolerance);
  }

  @Test
  void h1MultiplicationOrderIsRxNegativeA3ThenRyA4ThenRzA5() {
    double[] raw = new double[] {0.0, 0.0, 0.0, 20.0, 30.0, 40.0};
    double[][] expected =
        multiply(
            multiply(rotationX(Math.toRadians(-20.0)), rotationY(Math.toRadians(30.0))),
            rotationZ(Math.toRadians(40.0)));

    assertMatrixEquals(expected, VisionIOLimelight.convertTargetPose(raw).getRotation());
  }

  @Test
  void noncommutativeFixtureDiffersFromReversedOrder() {
    double x = Math.toRadians(-20.0);
    double y = Math.toRadians(30.0);
    double z = Math.toRadians(40.0);
    double[][] h1 = multiply(multiply(rotationX(x), rotationY(y)), rotationZ(z));
    double[][] reversed = multiply(multiply(rotationZ(z), rotationY(y)), rotationX(x));

    assertNotEquals(h1[0][1], reversed[0][1], kTolerance);
    assertNotEquals(h1[1][0], reversed[1][0], kTolerance);
  }

  @Test
  void knownCapturedTupleUsesTheLockedTranslationAndRotation() {
    double[] raw = new double[] {0.43, -0.27, 2.15, -3.0, 4.0, 5.0};
    Transform3d actual = VisionIOLimelight.convertTargetPose(raw);
    double[][] expected =
        multiply(
            multiply(rotationX(Math.toRadians(3.0)), rotationY(Math.toRadians(4.0))),
            rotationZ(Math.toRadians(5.0)));

    assertEquals(2.15, actual.getX(), kTolerance);
    assertEquals(-0.43, actual.getY(), kTolerance);
    assertEquals(0.27, actual.getZ(), kTolerance);
    assertMatrixEquals(expected, actual.getRotation());
  }

  @Test
  void validVisibleTargetProducesOneCameraToTargetInput() {
    table.getEntry(kTv).setDouble(1.0);
    table.getEntry(kTid).setDouble(7.0);
    table.getEntry(kTargetPose).setDoubleArray(new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});

    VisionIOInputs inputs = update();

    assertTrue(inputs.available);
    assertTrue(inputs.connected);
    assertTrue(inputs.sampleValid);
    assertEquals(1, inputs.targets.size());
    assertEquals(7, inputs.targets.get(0).tagId());
    assertEquals(3.0, inputs.targets.get(0).cameraToTarget().getX(), kTolerance);
  }

  @Test
  void progressingHeartbeatWithCachedTargetIsRejected() {
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});
    assertTrue(update().sampleValid);

    table.getEntry(kHeartbeat).setDouble(2.0);

    VisionIOInputs cached = update();

    assertFalse(cached.sampleValid);
    assertTrue(cached.targets.isEmpty());
  }

  @Test
  void targetRefreshedWhileHeartbeatStalledCannotQualifyRecovery() {
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});
    assertTrue(update().sampleValid);

    table.getEntry(kTv).setDouble(1.0);
    table.getEntry(kTid).setDouble(8.0);
    table.getEntry(kTargetPose)
        .setDoubleArray(new double[] {4.0, 5.0, 6.0, 0.0, 0.0, 0.0});
    assertFalse(update().sampleValid);

    table.getEntry(kHeartbeat).setDouble(2.0);

    VisionIOInputs recovery = update();

    assertFalse(recovery.sampleValid);
    assertTrue(recovery.targets.isEmpty());
  }

  @Test
  void progressingHeartbeatWithCompleteFreshTargetIsAccepted() {
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});
    assertTrue(update().sampleValid);

    setValidTarget(2.0, 8.0, new double[] {4.0, 5.0, 6.0, 0.0, 0.0, 0.0});

    VisionIOInputs refreshed = update();

    assertTrue(refreshed.sampleValid);
    assertEquals(8, refreshed.targets.get(0).tagId());
  }

  @Test
  void tvZeroProducesNoTargetMeasurement() {
    table.getEntry(kTv).setDouble(0.0);
    table.getEntry(kTargetPose).setDoubleArray(new double[6]);

    VisionIOInputs inputs = update();

    assertTrue(inputs.available);
    assertTrue(inputs.connected);
    assertFalse(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void repeatedHeartbeatInvalidatesTheSampleAndEventuallyFailsClosed() {
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});
    VisionIOInputs first = update();
    assertTrue(first.sampleValid);

    VisionIOInputs repeatedOnce = update();
    assertTrue(repeatedOnce.available);
    assertTrue(repeatedOnce.connected);
    assertFalse(repeatedOnce.sampleValid);
    assertTrue(repeatedOnce.targets.isEmpty());

    update();
    VisionIOInputs stalled = update();
    assertFalse(stalled.available);
    assertFalse(stalled.connected);
    assertFalse(stalled.sampleValid);
    assertTrue(stalled.targets.isEmpty());
  }

  @Test
  void heartbeatProgressionRestoresValidAcquisitionAfterStall() {
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});
    assertTrue(update().sampleValid);

    update();
    update();
    update();
    setValidTarget(2.0, 8.0, new double[] {4.0, 5.0, 6.0, 0.0, 0.0, 0.0});

    VisionIOInputs restored = update();

    assertTrue(restored.available);
    assertTrue(restored.connected);
    assertTrue(restored.sampleValid);
    assertEquals(8, restored.targets.get(0).tagId());
  }

  @Test
  void recoveryWithOnlyChangedTagIdIsRejected() {
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});
    assertTrue(update().sampleValid);
    update();
    update();
    update();

    table.getEntry(kTid).setDouble(8.0);
    table.getEntry(kHeartbeat).setDouble(2.0);

    assertFalse(update().sampleValid);
  }

  @Test
  void recoveryWithOnlyChangedVisibilityIsRejected() {
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});
    assertTrue(update().sampleValid);
    update();
    update();
    update();

    table.getEntry(kTv).setDouble(0.0);
    table.getEntry(kTv).setDouble(1.0);
    table.getEntry(kHeartbeat).setDouble(2.0);

    assertFalse(update().sampleValid);
  }

  @Test
  void heartbeatRecoveryWithStalePoseIsRejected() {
    setValidTarget(5.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});
    assertTrue(update().sampleValid);

    table.getEntry(kHeartbeat).setDouble(0.0);
    assertFalse(update().sampleValid);
    table.getEntry(kHeartbeat).setDouble(1.0);

    assertFalse(update().sampleValid);
  }

  @Test
  void doubleReadPoseChangeIsRejected() {
    AtomicBoolean changed = new AtomicBoolean();
    adapter =
        new VisionIOLimelight(
            table,
            () -> {
              if (changed.compareAndSet(false, true)) {
                table.getEntry(kTargetPose).setDoubleArray(new double[] {4.0, 5.0, 6.0, 0.0, 0.0, 0.0});
              }
            });
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});

    assertFalse(update().sampleValid);
  }

  @Test
  void doubleReadTagIdChangeIsRejected() {
    AtomicBoolean changed = new AtomicBoolean();
    adapter =
        new VisionIOLimelight(
            table,
            () -> {
              if (changed.compareAndSet(false, true)) {
                table.getEntry(kTid).setDouble(8.0);
              }
            });
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});

    assertFalse(update().sampleValid);
  }

  @Test
  void doubleReadVisibilityChangeIsRejected() {
    AtomicBoolean changed = new AtomicBoolean();
    adapter =
        new VisionIOLimelight(
            table,
            () -> {
              if (changed.compareAndSet(false, true)) {
                table.getEntry(kTv).unpublish();
              }
            });
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});

    assertFalse(update().sampleValid);
  }

  @Test
  void doubleReadTimestampChangeWithSameValueIsRejected() {
    AtomicBoolean changed = new AtomicBoolean();
    adapter =
        new VisionIOLimelight(
            table,
            () -> {
              if (changed.compareAndSet(false, true)) {
                // Re-publish the same value so only the NetworkTables change metadata changes.
                table.getEntry(kTid).unpublish();
                table.getEntry(kTid).setDouble(7.0);
              }
            });
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});

    assertFalse(update().sampleValid);
  }

  @Test
  void reconnectDoesNotReuseStaleTargetValues() {
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});
    assertTrue(update().sampleValid);

    table.getEntry(kHeartbeat).unpublish();
    assertFalse(update().available);

    table.getEntry(kHeartbeat).setDouble(2.0);
    VisionIOInputs staleAfterReconnect = update();
    assertTrue(staleAfterReconnect.available);
    assertTrue(staleAfterReconnect.connected);
    assertFalse(staleAfterReconnect.sampleValid);
    assertTrue(staleAfterReconnect.targets.isEmpty());

    setValidTarget(3.0, 9.0, new double[] {4.0, 5.0, 6.0, 0.0, 0.0, 0.0});
    VisionIOInputs refreshed = update();
    assertTrue(refreshed.sampleValid);
    assertEquals(9, refreshed.targets.get(0).tagId());
  }

  @Test
  void heartbeatResetDoesNotReuseStaleTargetValues() {
    setValidTarget(5.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});
    assertTrue(update().sampleValid);

    table.getEntry(kHeartbeat).setDouble(0.0);
    VisionIOInputs resetWithStaleTarget = update();
    assertFalse(resetWithStaleTarget.sampleValid);
    assertTrue(resetWithStaleTarget.targets.isEmpty());

    setValidTarget(1.0, 8.0, new double[] {4.0, 5.0, 6.0, 0.0, 0.0, 0.0});
    VisionIOInputs refreshed = update();
    assertTrue(refreshed.sampleValid);
    assertEquals(8, refreshed.targets.get(0).tagId());
  }

  @Test
  void mixedStaleTargetValuesFailClosedWhenFreshEvidenceIsRequired() {
    setValidTarget(1.0, 7.0, new double[] {1.0, 2.0, 3.0, 0.0, 0.0, 0.0});
    assertTrue(update().sampleValid);

    update();
    update();
    update();
    table.getEntry(kTv).setDouble(1.0);
    table.getEntry(kHeartbeat).setDouble(2.0);

    VisionIOInputs mixed = update();
    assertTrue(mixed.available);
    assertTrue(mixed.connected);
    assertFalse(mixed.sampleValid);
    assertTrue(mixed.targets.isEmpty());
  }

  @Test
  void malformedTargetPoseLengthFailsClosed() {
    table.getEntry(kTv).setDouble(1.0);
    table.getEntry(kTid).setDouble(7.0);
    table.getEntry(kTargetPose).setDoubleArray(new double[] {1.0, 2.0, 3.0});

    VisionIOInputs inputs = update();

    assertFalse(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void nanTargetPoseFailsClosed() {
    table.getEntry(kTv).setDouble(1.0);
    table.getEntry(kTid).setDouble(7.0);
    table.getEntry(kTargetPose).setDoubleArray(new double[] {1.0, Double.NaN, 3.0, 0.0, 0.0, 0.0});

    VisionIOInputs inputs = update();

    assertFalse(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void infinityTargetPoseFailsClosed() {
    table.getEntry(kTv).setDouble(1.0);
    table.getEntry(kTid).setDouble(7.0);
    table.getEntry(kTargetPose).setDoubleArray(new double[] {1.0, 2.0, Double.POSITIVE_INFINITY, 0.0, 0.0, 0.0});

    VisionIOInputs inputs = update();

    assertFalse(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void zeroTargetPoseWithNoTargetDoesNotManufactureMeasurement() {
    table.getEntry(kTv).setDouble(0.0);
    table.getEntry(kTargetPose).setDoubleArray(new double[6]);

    VisionIOInputs inputs = update();

    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void invalidTagIdFailsClosed() {
    table.getEntry(kTv).setDouble(1.0);
    table.getEntry(kTid).setDouble(0.0);
    table.getEntry(kTargetPose).setDoubleArray(new double[6]);

    VisionIOInputs inputs = update();

    assertFalse(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void missingHeartbeatFailsClosedAsUnavailable() {
    table.getEntry(kHeartbeat).unpublish();
    table.getEntry(kTv).setDouble(1.0);
    table.getEntry(kTid).setDouble(7.0);
    table.getEntry(kTargetPose).setDoubleArray(new double[6]);

    VisionIOInputs inputs = update();

    assertFalse(inputs.available);
    assertFalse(inputs.connected);
    assertFalse(inputs.sampleValid);
    assertTrue(inputs.targets.isEmpty());
  }

  @Test
  void publicContractsDoNotExposeLimelightOrVendorTypes() {
    assertTrue(
        Arrays.stream(VisionIO.class.getMethods())
            .flatMap(method -> Arrays.stream(method.getParameterTypes()))
            .map(Class::getName)
            .noneMatch(name -> name.toLowerCase().contains("limelight")));
    assertTrue(
        Arrays.stream(frc.robot.Constants.VisionConstants.class.getFields())
            .map(Field::getName)
            .noneMatch(name -> name.toLowerCase().contains("limelight")));
    assertTrue(
        Arrays.stream(VisionIO.class.getMethods())
            .map(Method::getReturnType)
            .map(Class::getName)
            .noneMatch(name -> name.toLowerCase().contains("limelight")));
    assertTrue(
        Arrays.stream(VisionObservation.class.getRecordComponents())
            .map(component -> component.getType().getName())
            .noneMatch(name -> name.toLowerCase().contains("limelight")));
  }

  @Test
  void simulationSelectionStillUsesVisionIOSim() throws ReflectiveOperationException {
    if (RobotBase.isReal()) {
      return;
    }

    RobotContainer container = new RobotContainer();
    Field visionIoField = RobotContainer.class.getDeclaredField("visionIO");
    visionIoField.setAccessible(true);

    assertNotNull(visionIoField.get(container));
    assertTrue(visionIoField.get(container) instanceof VisionIOSim);
  }

  @Test
  void realAdapterExposesCameraToTargetWithoutInversion() {
    table.getEntry(kTv).setDouble(1.0);
    table.getEntry(kTid).setDouble(7.0);
    table.getEntry(kTargetPose).setDoubleArray(new double[] {1.0, 2.0, 3.0, 10.0, 20.0, 30.0});

    VisionTargetInputs target = update().targets.get(0);

    assertEquals(3.0, target.cameraToTarget().getX(), kTolerance);
    assertEquals(-1.0, target.cameraToTarget().getY(), kTolerance);
    assertEquals(-2.0, target.cameraToTarget().getZ(), kTolerance);
    assertEquals(
        VisionIOLimelight.convertTargetPose(new double[] {1.0, 2.0, 3.0, 10.0, 20.0, 30.0}),
        target.cameraToTarget());
  }

  private VisionIOInputs update() {
    VisionIOInputs inputs = new VisionIOInputs();
    adapter.updateInputs(inputs);
    return inputs;
  }

  private void setValidTarget(double heartbeat, double tagId, double[] targetPose) {
    table.getEntry(kTv).setDouble(1.0);
    table.getEntry(kTid).setDouble(tagId);
    table.getEntry(kTargetPose).setDoubleArray(targetPose);
    table.getEntry(kHeartbeat).setDouble(heartbeat);
  }

  private static void assertMatrixEquals(double[][] expected, Rotation3d actual) {
    for (int row = 0; row < 3; row++) {
      for (int column = 0; column < 3; column++) {
        assertEquals(expected[row][column], actual.toMatrix().get(row, column), kTolerance);
      }
    }
  }

  private static double[][] rotationX(double angle) {
    double cosine = Math.cos(angle);
    double sine = Math.sin(angle);
    return new double[][] {
      {1.0, 0.0, 0.0},
      {0.0, cosine, -sine},
      {0.0, sine, cosine}
    };
  }

  private static double[][] rotationY(double angle) {
    double cosine = Math.cos(angle);
    double sine = Math.sin(angle);
    return new double[][] {
      {cosine, 0.0, sine},
      {0.0, 1.0, 0.0},
      {-sine, 0.0, cosine}
    };
  }

  private static double[][] rotationZ(double angle) {
    double cosine = Math.cos(angle);
    double sine = Math.sin(angle);
    return new double[][] {
      {cosine, -sine, 0.0},
      {sine, cosine, 0.0},
      {0.0, 0.0, 1.0}
    };
  }

  private static double[][] multiply(double[][] left, double[][] right) {
    double[][] result = new double[3][3];
    for (int row = 0; row < 3; row++) {
      for (int column = 0; column < 3; column++) {
        for (int index = 0; index < 3; index++) {
          result[row][column] += left[row][index] * right[index][column];
        }
      }
    }
    return result;
  }
}
