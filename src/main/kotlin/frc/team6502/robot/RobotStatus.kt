package frc.team6502.robot

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commands.manip.SetElevatorOffset

object RobotStatus {
    var currentGamePiece = GamePiece.NONE
        private set

    var none
        get() = SmartDashboard.getBoolean("None", false)
        set(v) {
            SmartDashboard.putBoolean("None",v)
        }

    var hatch
        get() = SmartDashboard.getBoolean("Hatch", false)
        set(v) {
            SmartDashboard.putBoolean("Hatch",v)
        }

    var cargo
        get() = SmartDashboard.getBoolean("Cargo", false)
        set(v) {
            SmartDashboard.putBoolean("Cargo",v)
        }

    init {
        none = false
        hatch = false
        cargo = false
    }

    fun setGamePiece(gp: GamePiece) {
        none = false
        hatch = false
        cargo = false

        currentGamePiece = gp
        when (currentGamePiece) {
            GamePiece.NONE -> SetElevatorOffset(ElevatorOffset.INTAKE).start()
            GamePiece.CARGO -> SetElevatorOffset(ElevatorOffset.CARGO_DELIVERY).start()
            GamePiece.HATCH -> SetElevatorOffset(ElevatorOffset.HATCH_DELIVERY).start()
        }
    }

}

