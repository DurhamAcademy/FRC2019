package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.*
import frc.team6502.robot.subsystems.CargoIntake
import frc.team6502.robot.subsystems.Elevator

class IntakeCargo(val loadingStation: Boolean = false) : Command() {

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
        if (loadingStation) {
            SetElevatorHeight(LOADING_STATION_HEIGHT).start()
            SetElevatorOffset(ElevatorOffset.INTAKE).start()
        }
        intakeCurrentTimer.reset()
        intakeCurrentTimer.start()
    }

    override fun execute() {
        if (!loadingStation) {
            if (Elevator.elevatorTalon.selectedSensorPosition < 2048) {
                CargoIntake.speedIntake = -0.4
                CargoIntake.speedShooter = 0.2
            } else {
                CargoIntake.speedIntake = 0.0
                CargoIntake.speedShooter = 0.0
            }
        } else {
            CargoIntake.speedShooter = -0.2
            CargoIntake.speedIntake = 0.0
        }
//        println("RUNNING RUNNING RUNNING")
        SmartDashboard.putNumber("shooterCurrent", CargoIntake.shooterCurrent)
    }

    override fun end() {
        CargoIntake.speedIntake = 0.0
        CargoIntake.speedShooter = 0.0

        singleton = null
        if (!loadingStation) ZeroCargo().start()
        else {
            OI.setElevatorHeight(OI.selectedElevatorHeight)
            SetGamePiece(GamePiece.NONE).start()
        }
    }

    override fun interrupted() {
        CargoIntake.speedIntake = 0.0
        CargoIntake.speedShooter = 0.0
        singleton = null
        if (loadingStation) OI.setElevatorHeight(OI.selectedElevatorHeight)
    }

    override fun isFinished(): Boolean {
        return (CargoIntake.shooterCurrent > 1.6 && intakeCurrentTimer.get() > 0.2)
    }
}