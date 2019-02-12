package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.subsystems.CargoIntake

object IntakeCargo : Command() {
    private val intakeCurrentThreshold = 100
    private val intakeCurrentTimer = Timer()

    init {
        requires(CargoIntake)
    }

    override fun start() {
        CargoIntake.speed = 0.5
    }

    override fun execute() {
        SmartDashboard.putNumber("current", CargoIntake.current)
    }

    override fun end() {
        CargoIntake.speed = 0.0
    }

    override fun interrupted() {
        CargoIntake.speed = 0.0
    }

    override fun isFinished(): Boolean {
        return CargoIntake.current > intakeCurrentThreshold
    }
}