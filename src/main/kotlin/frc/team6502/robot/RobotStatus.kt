package frc.team6502.robot

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commands.manip.SetElevatorOffset

object RobotStatus {
    var currentGamePiece = GamePiece.NONE
        private set

    fun setGamePiece(gp: GamePiece) {
        SmartDashboard.putBoolean("None", currentGamePiece == GamePiece.NONE)
        SmartDashboard.putBoolean("Cargo", currentGamePiece == GamePiece.CARGO)
        SmartDashboard.putBoolean("Panel", currentGamePiece == GamePiece.HATCH)

        currentGamePiece = gp
        when (currentGamePiece) {
            GamePiece.NONE -> SetElevatorOffset(ElevatorOffset.INTAKE).start()
            GamePiece.CARGO -> SetElevatorOffset(ElevatorOffset.CARGO_DELIVERY).start()
            GamePiece.HATCH -> SetElevatorOffset(ElevatorOffset.HATCH_DELIVERY).start()
        }
    }

    init {
        SmartDashboard.putBoolean("None", currentGamePiece == GamePiece.NONE)
        SmartDashboard.putBoolean("Cargo", currentGamePiece == GamePiece.CARGO)
        SmartDashboard.putBoolean("Panel", currentGamePiece == GamePiece.HATCH)
    }

}

