package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.kyberlib.util.units.inches
import frc.team6502.robot.OI
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
        CargoIntake.speedIntake = 0.5
        intakeCurrentTimer.reset()
        intakeCurrentTimer.start()
    }

    override fun execute() {
//        println("RUNNING RUNNING RUNNING")
        SmartDashboard.putNumber("shooterCurrent", CargoIntake.shooterCurrent)
        SetElevatorHeight(6.inches).start()
    }

    override fun end() {
        CargoIntake.speedIntake = 0.0
        singleton = null
        OI.setElevatorHeight(0)
    }

    override fun interrupted() {
        CargoIntake.speedIntake = 0.0
        singleton = null
        OI.setElevatorHeight(0)
    }

    override fun isFinished(): Boolean {
        println(intakeCurrentTimer.get())
        return OI.controller.yButton || (CargoIntake.shooterCurrent > 2.0 && intakeCurrentTimer.get() > 1.0)
    }
}