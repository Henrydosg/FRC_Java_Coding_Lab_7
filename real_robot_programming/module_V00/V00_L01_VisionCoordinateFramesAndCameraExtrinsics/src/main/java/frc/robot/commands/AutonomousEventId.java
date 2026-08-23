// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.commands;

/** Stable identifiers for the narrowly scoped L09 autonomous learning events. */
public enum AutonomousEventId {
  /** The one mechanism-independent event used to teach marker dispatch. */
  LEARNING_EVENT("LEARNING_EVENT");

  private final String stableName;

  AutonomousEventId(String stableName) {
    this.stableName = stableName;
  }

  /** Returns the stable PathPlanner NamedCommands name for this event. */
  public String stableName() {
    return stableName;
  }
}
