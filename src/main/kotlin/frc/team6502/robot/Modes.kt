package frc.team6502.robot

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

enum class DrivetrainMode {
    DISABLED, DEMO, OPEN_LOOP, CLOSED_LOOP
}

enum class ElevatorMode {
    DISABLED, MANUAL, POSITIONAL, MOTIONMAGIC
}

object Modes {
    val drivetrainMode = SendableChooser<DrivetrainMode>()
    val elevatorMode = SendableChooser<ElevatorMode>()

    init {
        drivetrainMode.setDefaultOption("Closed Loop", DrivetrainMode.CLOSED_LOOP)
        drivetrainMode.addOption("Open Loop", DrivetrainMode.OPEN_LOOP)
        drivetrainMode.addOption("Demo", DrivetrainMode.DEMO)
        drivetrainMode.addOption("Disabled", DrivetrainMode.DISABLED)

        elevatorMode.setDefaultOption("Disabled", ElevatorMode.DISABLED)
        elevatorMode.addOption("Manual", ElevatorMode.MANUAL)
        elevatorMode.addOption("Positional", ElevatorMode.POSITIONAL)
        elevatorMode.addOption("Motion Magic", ElevatorMode.MOTIONMAGIC)


        SmartDashboard.putData("Drivetrain Mode", drivetrainMode)
        SmartDashboard.putData("Elevator Mode", elevatorMode)
    }
}

