package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.GamePiece
import frc.team6502.robot.RobotStatus

class SetGamePiece(val gp: GamePiece) : InstantCommand() {
    override fun execute() {
        RobotStatus.setGamePiece(gp)
    }
}