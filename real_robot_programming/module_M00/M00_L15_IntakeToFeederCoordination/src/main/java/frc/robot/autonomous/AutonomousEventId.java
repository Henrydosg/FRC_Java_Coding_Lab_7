// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

/**
 * Author: SSIS
 * Mentor: SSIS
 */

package frc.robot.autonomous;

import java.util.Objects;

/** Vendor-neutral identity for one autonomous event understood by the lesson. */
public enum AutonomousEventId {
  LEARNING_EVENT("LEARNING_EVENT");

  private final String pathPlannerName;

  AutonomousEventId(String pathPlannerName) {
    this.pathPlannerName = Objects.requireNonNull(pathPlannerName, "pathPlannerName");
  }

  /** Returns the stable name used by the PathPlanner NamedCommands registry. */
  public String pathPlannerName() {
    return pathPlannerName;
  }
}
