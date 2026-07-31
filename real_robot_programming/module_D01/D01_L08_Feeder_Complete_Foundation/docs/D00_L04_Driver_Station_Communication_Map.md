# D00_L04 Driver Station Communication Map

## 1. Lesson Purpose

Explain how driver input travels from the Driver Station to the roboRIO, identify the supported connection paths, and define a safe verification boundary without changing robot behavior or the frozen Java architecture.

## 2. System Communication Flow

```text
Driver
-> Xbox Controller
-> Driver Station
-> Network connection
-> roboRIO
-> Robot program
-> DefaultDriveCommand
-> DriveSubsystem
-> DriveIO
-> Motor controllers
```

The Driver Station and network transport deliver controller state to WPILib on the roboRIO. Inside the robot program, `DefaultDriveCommand` uses `DriveInputProcessor` before sending drive requests to `DriveSubsystem`. The subsystem continues through the vendor-independent `DriveIO` boundary to the motor controllers.

## 3. Connection Paths

### A. USB Tether

- Expected roboRIO address: `172.22.11.2`
- Best use: Initial setup and troubleshooting
- Internet availability: Not required

### B. Direct Ethernet

- Expected team subnet: `10.109.51.x`
- Expected roboRIO address: `10.109.51.2`
- Best use: Stable wired verification

### C. Team Radio / Wireless

- The Driver Station and roboRIO communicate through the configured team radio.
- Team identity: `10951`
- Best use: Normal field-style operation

## 4. Robot Identity

- Team number: `10951`
- Hostname: `roboRIO-10951-FRC.local`

The Driver Station, WPILib project, roboRIO image, and radio configuration must use the same team identity.

## 5. Driver Station Responsibilities

- Read the Xbox controller assigned to the correct USB port.
- Use team number `10951` to identify the robot.
- Send control packets and requested robot mode over the active connection.
- Display communication, robot-code, joystick, and enable-state indicators.
- Provide Disable and Emergency Stop controls for safe operation.

## 6. roboRIO Responsibilities

- Receive Driver Station control packets.
- Run the deployed WPILib robot program.
- Apply Disabled, Autonomous, Teleop, and Test state transitions.
- Run the command scheduler and the existing frozen control pipeline.
- Stop enabled operation when valid Driver Station communication is lost.

## 7. What Simulation Can Verify

- The robot project starts in HAL simulation.
- A simulated Driver Station can connect to the simulated robot process.
- Disabled, Autonomous, Teleop, and Test state transitions reach the robot program.
- Controller values can enter the inherited command pipeline.
- The Java control architecture builds and runs without physical networking hardware.

Simulation does not prove the physical roboRIO identity, USB driver, Ethernet wiring, radio configuration, wireless quality, or real Driver Station-to-robot communication.

## 8. What Requires Real Hardware

- Connecting to a roboRIO at `172.22.11.2` over USB.
- Reaching the team subnet and `10.109.51.2` over Ethernet or radio.
- Resolving `roboRIO-10951-FRC.local`.
- Confirming the roboRIO is imaged for team `10951`.
- Confirming Driver Station communication and robot-code indicators.
- Verifying the configured team radio and wireless path.
- Observing real latency, packet loss, disconnection, and reconnection behavior.

No real-hardware verification is claimed in this lesson step.

## 9. Safety Rules

- Keep the robot disabled while changing connections.
- Raise the drivetrain securely before enabling.
- Keep an operator ready to disable the robot.
- Test one connection method at a time.
- Center the controller and keep people clear of the drivetrain before enabling.
- Start with USB or direct Ethernet before troubleshooting wireless operation.
