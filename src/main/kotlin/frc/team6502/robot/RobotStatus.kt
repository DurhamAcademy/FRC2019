package frc.team6502.robot

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import frc.team6502.robot.commands.manip.SetElevatorOffset

object RobotStatus {
    var currentGamePiece = GamePiece.NONE
        private set

    val none = Shuffleboard.getTab("Teleop")
            .add("None", false)
            .withWidget("Toggle Button")
            .entry

    val hatch = Shuffleboard.getTab("Teleop")
            .add("Hatch", false)
            .withWidget("Toggle Button")
            .entry

    val cargo = Shuffleboard.getTab("Teleop")
            .add("Cargo", false)
            .withWidget("Toggle Button")
            .entry

    fun setGamePiece(gp: GamePiece) {
        none.setBoolean(false)
        hatch.setBoolean(false)
        cargo.setBoolean(false)

        currentGamePiece = gp
        when (currentGamePiece) {
            GamePiece.NONE -> SetElevatorOffset(ElevatorOffset.INTAKE).start()
            GamePiece.CARGO -> SetElevatorOffset(ElevatorOffset.CARGO_DELIVERY).start()
            GamePiece.HATCH -> SetElevatorOffset(ElevatorOffset.HATCH_DELIVERY).start()
        }
    }

}

