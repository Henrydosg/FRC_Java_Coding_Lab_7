// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.AutonomousRoutineFactory;
import frc.robot.commands.AutonomousSafetyHoldCommand;
import java.lang.reflect.Field;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class RobotContainerAutonomousRoutineSelectionTest {
  @BeforeAll
  static void initializeHal() {
    HAL.initialize(500, 0);
  }

  @AfterEach
  void resetAutoBuilder() {
    AutoBuilder.resetForTesting();
  }

  @Test
  void publishesChooserWithSafeStopAsDefaultAndCreatesFreshCommands() throws Exception {
    RobotContainer robotContainer = new RobotContainer();

    @SuppressWarnings("unchecked")
    SendableChooser<AutonomousRoutineFactory.AutonomousRoutineId> chooser =
        (SendableChooser<AutonomousRoutineFactory.AutonomousRoutineId>)
            SmartDashboard.getData("Autonomous Routine");
    assertSame(AutonomousRoutineFactory.AutonomousRoutineId.SAFE_STOP, chooser.getSelected());

    Command first = robotContainer.getAutonomousCommand();
    Command second = robotContainer.getAutonomousCommand();
    assertInstanceOf(AutonomousSafetyHoldCommand.class, first);
    assertInstanceOf(AutonomousSafetyHoldCommand.class, second);
    assertNotSame(first, second);
  }

  @Test
  void chooserSnapshotDoesNotMutateAlreadyCreatedCommand() throws Exception {
    RobotContainer robotContainer = new RobotContainer();
    @SuppressWarnings("unchecked")
    SendableChooser<AutonomousRoutineFactory.AutonomousRoutineId> chooser =
        (SendableChooser<AutonomousRoutineFactory.AutonomousRoutineId>)
            SmartDashboard.getData("Autonomous Routine");

    Command snapshotted = robotContainer.getAutonomousCommand();
    Field selectedField = SendableChooser.class.getDeclaredField("m_selected");
    selectedField.setAccessible(true);
    selectedField.set(chooser, "ONE_METER_PATH");

    assertInstanceOf(AutonomousSafetyHoldCommand.class, snapshotted);
    assertSame(
        AutonomousRoutineFactory.AutonomousRoutineId.ONE_METER_PATH,
        chooser.getSelected());
  }
}
