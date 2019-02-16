package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.OI
import frc.team6502.robot.subsystems.CargoIntake

class IntakeCargo : Command() {
    private val intakeCurrentThreshold = 15
    private val intakeCurrentTimer = Timer()

    companion object {
        var singleton: Command? = null
    }

    init {
        singleton = this
    }

    override fun initialize() {
        CargoIntake.speed = 0.5
        intakeCurrentTimer.reset()
        intakeCurrentTimer.start()
    }

    override fun execute() {
        println("RUNNING RUNNING RUNNING")
        SmartDashboard.putNumber("current", CargoIntake.current)
    }

    override fun end() {
        CargoIntake.speed = 0.0
        singleton = null
    }

    override fun interrupted() {
        CargoIntake.speed = 0.0
        singleton = null
    }

    override fun isFinished(): Boolean {
        println(intakeCurrentTimer.get())
        return OI.controller.yButton || (CargoIntake.current > 2.0 && intakeCurrentTimer.get() > 1.0)
    }
}