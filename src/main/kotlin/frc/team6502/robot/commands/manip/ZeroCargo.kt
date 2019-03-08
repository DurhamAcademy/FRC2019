package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.TimedCommand
import frc.team6502.robot.GamePiece
import frc.team6502.robot.RobotStatus
import frc.team6502.robot.subsystems.CargoIntake

class ZeroCargo : TimedCommand(1.0) {
    init {
        requires(CargoIntake)
    }

    override fun initialize() {
        //Make the shooter reverse
        CargoIntake.shooterDrection(true)
        CargoIntake.speedShooter = 0.1
    }

    override fun end() {
        CargoIntake.speedShooter = 0.0
        CargoIntake.shooterDrection(false)
        RobotStatus.setGamePiece(GamePiece.CARGO)
    }

    override fun interrupted() {
        CargoIntake.speedShooter = 0.0
        CargoIntake.shooterDrection(false)
    }
}