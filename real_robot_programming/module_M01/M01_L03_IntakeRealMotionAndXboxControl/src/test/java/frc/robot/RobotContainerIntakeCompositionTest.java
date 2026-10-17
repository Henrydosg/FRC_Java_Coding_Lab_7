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
import frc.robot.io.intake.IntakeIO;
import frc.robot.io.intake.IntakeIONoop;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.telemetry.RobotTelemetry;
import frc.robot.telemetry.intake.IntakeTelemetryFacade;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/** Verifies Noop Intake composition without command or binding ownership. */
class RobotContainerIntakeCompositionTest {
  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @Test
  void composesNoopIntakeAndConnectsReadOnlyTelemetry()
      throws ReflectiveOperationException {
    NamedCommands.clearAll();
    AutoBuilder.resetForTesting();
    try {
      RobotContainer robotContainer = new RobotContainer();
      IntakeSubsystem intakeSubsystem =
          (IntakeSubsystem) field(RobotContainer.class, "intakeSubsystem").get(robotContainer);
      IntakeIO intakeIO =
          (IntakeIO) field(IntakeSubsystem.class, "intakeIO").get(intakeSubsystem);
      RobotTelemetry robotTelemetry = robotContainer.getRobotTelemetry();

      assertInstanceOf(IntakeIONoop.class, intakeIO);
      assertNull(intakeSubsystem.getDefaultCommand());

      @SuppressWarnings("unchecked")
      Optional<IntakeSubsystem> telemetryIntakeSubsystem =
          (Optional<IntakeSubsystem>)
              field(RobotTelemetry.class, "intakeSubsystem").get(robotTelemetry);
      @SuppressWarnings("unchecked")
      Optional<IntakeTelemetryFacade> telemetryFacade =
          (Optional<IntakeTelemetryFacade>)
              field(RobotTelemetry.class, "intakeTelemetryFacade").get(robotTelemetry);

      assertTrue(telemetryIntakeSubsystem.isPresent());
      assertSame(intakeSubsystem, telemetryIntakeSubsystem.orElseThrow());
      assertTrue(telemetryFacade.isPresent());

      intakeSubsystem.periodic();
      robotTelemetry.periodic();
      NetworkTable intakeTable = NetworkTableInstance.getDefault().getTable("Intake");
      assertFalse(intakeTable.getEntry("Available").getBoolean(true));
      assertFalse(intakeTable.getEntry("Connected").getBoolean(true));
      assertEquals("STOPPED", intakeTable.getEntry("RequestedState").getString(""));
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
