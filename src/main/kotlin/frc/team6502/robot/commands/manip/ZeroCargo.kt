package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.TimedCommand
import frc.team6502.robot.GamePiece
import frc.team6502.robot.RobotStatus
import frc.team6502.robot.subsystems.CargoIntake

class ZeroCargo : TimedCommand(1.0) {
    init {
//        requires(CargoIntake)
    }

    override fun initialize() {
        //Make the shooter reverse
        println("I AM ZEROING")
        CargoIntake.speedShooter = -0.25
    }

    override fun end() {
        CargoIntake.speedShooter = 0.0
        RobotStatus.setGamePiece(GamePiece.CARGO)
    }

    override fun interrupted() {
        CargoIntake.speedShooter = 0.0
    }
}