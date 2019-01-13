package frc.team6502.robot.commands

import edu.wpi.first.wpilibj.command.InstantCommand

class SetIntakeSpeed(speed: Double): InstantCommand() {

    init {
//        requires(RobotMap.kIntake)
    }

    override fun execute() {
        // RobotMap.kIntake.speed = speed
    }
}