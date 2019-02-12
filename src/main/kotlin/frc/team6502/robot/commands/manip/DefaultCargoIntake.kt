package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.Command
import frc.team6502.robot.OI
import frc.team6502.robot.subsystems.CargoIntake

class DefaultCargoIntake() : Command() {
    override fun isFinished() = false

    init {
        requires(CargoIntake)
    }

    override fun execute() {
        CargoIntake.speed = when (OI.controller.pov) {
            0 -> 0.5
            180 -> -0.5
            else -> 0.0
        }
    }
}