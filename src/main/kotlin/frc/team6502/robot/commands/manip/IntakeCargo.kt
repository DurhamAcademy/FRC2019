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
        println("initializing intake")
        RobotStatus.setStatusCargo(if (loadingStation) CargoStatus.INTAKING_STATION else CargoStatus.INTAKING_GROUND)
        OI.setElevatorHeight(0)
        intakeCurrentTimer.reset()
        intakeCurrentTimer.start()
    }

    override fun execute() {
        println("executing intake")
        if (!loadingStation) {

            if (Elevator.elevatorTalon.selectedSensorPosition < 1024) {
                CargoIntake.speedIntake = -0.5
                CargoIntake.speedShooter = 0.2
            } else {
                CargoIntake.speedShooter = 0.0
                CargoIntake.speedIntake = 0.0
            }
        } else {
            CargoIntake.speedShooter = -0.2
            CargoIntake.speedIntake = 0.0
        }

//        println("RUNNING RUNNING RUNNING")

    }

    override fun end() {
        CargoIntake.speedIntake = 0.0
        CargoIntake.speedShooter = 0.0

        println("ENDING INTAKE")
        singleton = null

        if (!loadingStation) ZeroCargo().start()
        else {
            RobotStatus.setStatusCargo(CargoStatus.IDLE)
        }
    }

    override fun interrupted() {
        println("INTERRUPTING INTAKE")
        CargoIntake.speedIntake = 0.0
        CargoIntake.speedShooter = 0.0
        singleton = null
        RobotStatus.setStatusCargo(CargoStatus.NONE)
    }

    override fun isFinished(): Boolean {
        return !OI.controller.bButton//(CargoIntake.shooterCurrent > 1.6 && intakeCurrentTimer.get() > 0.2 && timeSinceInitialized() > 0.2)
    }
}