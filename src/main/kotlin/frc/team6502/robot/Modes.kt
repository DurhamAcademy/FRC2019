package frc.team6502.robot

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

enum class DrivetrainMode {
    DISABLED, DEMO, OPEN_LOOP, CLOSED_LOOP
}

object Modes {
    val drivetrainMode = SendableChooser<DrivetrainMode>()

    init {
        drivetrainMode.setDefaultOption("Closed Loop", DrivetrainMode.CLOSED_LOOP)
        drivetrainMode.addOption("Open Loop", DrivetrainMode.OPEN_LOOP)
        drivetrainMode.addOption("Demo", DrivetrainMode.DEMO)
        drivetrainMode.addOption("Disabled", DrivetrainMode.DISABLED)

        SmartDashboard.putData(drivetrainMode)
    }
}

