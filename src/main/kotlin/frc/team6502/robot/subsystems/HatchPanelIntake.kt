package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.AnalogInput
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.robot.commands.defaults.DefaultHatchPanelIntake

object HatchPanelIntake : Subsystem() {
    private val solenoidA = DoubleSolenoid(0, 1)
    private val solenoidB = DoubleSolenoid(2, 3)

    private val ultrasonic = AnalogInput(0)

    fun setCylinders(extended: Boolean) {
        val value: DoubleSolenoid.Value = when (extended) {
            true -> DoubleSolenoid.Value.kForward
            false -> DoubleSolenoid.Value.kReverse
        }
        solenoidA.set(value)
        solenoidB.set(value)
    }

    val ultrasonicDistance
        get() = ultrasonic.value

    override fun initDefaultCommand() {
        defaultCommand = DefaultHatchPanelIntake()
    }

}