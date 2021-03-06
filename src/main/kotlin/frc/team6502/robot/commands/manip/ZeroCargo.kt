package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.TimedCommand
import frc.team6502.robot.CargoStatus
import frc.team6502.robot.RobotStatus
import frc.team6502.robot.subsystems.CargoIntake

class ZeroCargo : TimedCommand(1.0) {
    init {
//        requires(CargoIntake)
    }

    override fun initialize() {
        //Make the shooter reverse
        CargoIntake.speedShooter = -0.2
    }

    override fun end() {
        CargoIntake.speedShooter = 0.0
        RobotStatus.setStatusCargo(CargoStatus.IDLE)
    }

    override fun interrupted() {
        CargoIntake.speedShooter = 0.0
    }
}