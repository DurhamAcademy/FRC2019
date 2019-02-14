package frc.team6502.robot

enum class DrivetrainMode {
    DISABLED, DEMO, OPEN_LOOP, CLOSED_LOOP
}

enum class ElevatorMode {
    DISABLED, MANUAL, POSITIONAL, MOTIONMAGIC
}

object Modes {
    val drivetrainMode = EnumChooser(DrivetrainMode::class.java)
    val elevatorMode = EnumChooser(ElevatorMode::class.java)
}

