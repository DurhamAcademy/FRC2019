package frc.team6502.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team6502.robot.OI
import frc.team6502.robot.subsystems.Intake

class DefaultIntake() : Command() {
    override fun isFinished() = false

    init {
        requires(Intake)
    }

    override fun execute() {
        Intake.speed = when (OI.controller.pov) {
            0 -> 0.5
            180 -> -0.5
            else -> 0.0
        }
    }
}