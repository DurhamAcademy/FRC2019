package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.TimedCommand
import frc.team6502.robot.subsystems.CargoIntake

class RejectCargo : TimedCommand(1.5) {
    init {
        requires(CargoIntake)
    }

    override fun execute() {
        CargoIntake.shooterDrection(true)
        CargoIntake.rampState = false
        CargoIntake.speedIntake = 1.0
    }

    override fun end() {
        CargoIntake.shooterDrection(false)
        CargoIntake.speedIntake = 0.0
    }

    override fun isFinished(): Boolean {
        return false
    }
}