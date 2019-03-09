package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.GamePiece
import frc.team6502.robot.RobotStatus
import frc.team6502.robot.subsystems.CargoIntake
import frc.team6502.robot.subsystems.Elevator

class IntakeCargo : Command() {
    private val intakeCurrentThreshold = 15
    private val intakeCurrentTimer = Timer()

    companion object {
        var singleton: Command? = null
    }

    init {
        singleton = this
        requires(CargoIntake)
    }

    override fun initialize() {
        if (RobotStatus.currentGamePiece != GamePiece.NONE) {
            cancel()
        }
        intakeCurrentTimer.reset()
        intakeCurrentTimer.start()
    }

    override fun execute() {
        if (Elevator.elevatorTalon.selectedSensorPosition < 2048) {
            CargoIntake.speedIntake = 0.5
        } else {
            CargoIntake.speedIntake = 0.0
        }
//        println("RUNNING RUNNING RUNNING")
        SmartDashboard.putNumber("shooterCurrent", CargoIntake.shooterCurrent)
    }

    override fun end() {
        CargoIntake.speedIntake = 0.0
        singleton = null
        ZeroCargo().start()
    }

    override fun interrupted() {
        CargoIntake.speedIntake = 0.0
        singleton = null
    }

    override fun isFinished(): Boolean {
        return (CargoIntake.shooterCurrent > 1.5 && intakeCurrentTimer.get() > 0.2)
    }
}