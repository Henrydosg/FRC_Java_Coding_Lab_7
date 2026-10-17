// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.robot.io.feeder.FeederIO;
import frc.robot.io.feeder.FeederIONoop;
import frc.robot.subsystems.FeederSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.feeder.FeederTelemetryFacade;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Verifies Noop Feeder composition without command or binding ownership. */
class RobotContainerFeederCompositionTest {
  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void composesNoopFeederAndConnectsReadOnlyTelemetry()
      throws ReflectiveOperationException {
    NamedCommands.clearAll();
    AutoBuilder.resetForTesting();
    try {
      RobotContainer robotContainer = new RobotContainer();
      FeederSubsystem feederSubsystem =
          (FeederSubsystem) field(RobotContainer.class, "feederSubsystem").get(robotContainer);
      FeederIO feederIO =
          (FeederIO) field(FeederSubsystem.class, "feederIO").get(feederSubsystem);
      RobotTelemetry robotTelemetry = robotContainer.getRobotTelemetry();

      assertInstanceOf(FeederIONoop.class, feederIO);
      assertNull(feederSubsystem.getDefaultCommand());

      @SuppressWarnings("unchecked")
      Optional<FeederSubsystem> telemetryFeederSubsystem =
          (Optional<FeederSubsystem>)
              field(RobotTelemetry.class, "feederSubsystem").get(robotTelemetry);
      @SuppressWarnings("unchecked")
      Optional<FeederTelemetryFacade> telemetryFacade =
          (Optional<FeederTelemetryFacade>)
              field(RobotTelemetry.class, "feederTelemetryFacade").get(robotTelemetry);

      assertTrue(telemetryFeederSubsystem.isPresent());
      assertSame(feederSubsystem, telemetryFeederSubsystem.orElseThrow());
      assertTrue(telemetryFacade.isPresent());

      feederSubsystem.periodic();
      robotTelemetry.periodic();
      NetworkTable feederTable = NetworkTableInstance.getDefault().getTable("Feeder");
      assertFalse(feederTable.getEntry("Available").getBoolean(true));
      assertFalse(feederTable.getEntry("Connected").getBoolean(true));
      assertEquals("STOPPED", feederTable.getEntry("RequestedState").getString(""));
    } finally {
      AutoBuilder.resetForTesting();
      NamedCommands.clearAll();
    }
  }

  private static Field field(Class<?> owner, String name) throws NoSuchFieldException {
    Field field = owner.getDeclaredField(name);
    field.setAccessible(true);
    return field;
  }
}
