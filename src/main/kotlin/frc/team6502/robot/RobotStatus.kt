package frc.team6502.robot

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commands.manip.SetElevatorOffset

object RobotStatus {
    var currentGamePiece = GamePiece.NONE
        private set

    fun setGamePiece(gp: GamePiece) {
        SmartDashboard.putBoolean("Has Cargo", currentGamePiece == GamePiece.CARGO)
        SmartDashboard.putBoolean("Has Panel", currentGamePiece == GamePiece.HATCH)

        currentGamePiece = gp
        when (currentGamePiece) {
            GamePiece.NONE -> SetElevatorOffset(ElevatorOffset.INTAKE).start()
            GamePiece.CARGO -> SetElevatorOffset(ElevatorOffset.CARGO_DELIVERY).start()
            GamePiece.HATCH -> SetElevatorOffset(ElevatorOffset.HATCH_DELIVERY).start()
        }
    }

    init {
        SmartDashboard.putBoolean("Has Cargo", currentGamePiece == GamePiece.CARGO)
        SmartDashboard.putBoolean("Has Panel", currentGamePiece == GamePiece.HATCH)
    }

}

