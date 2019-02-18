package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.Command
import frc.team6502.robot.subsystems.CargoIntake

class ZeroCargo : Command() {
    init {
        requires(CargoIntake)
    }

    override fun initialize() {
        //Make the shooter reverse
    }
    override fun isFinished(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}