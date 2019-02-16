package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.command.Subsystem

object HatchPanelIntake : Subsystem() {
    override fun initDefaultCommand() {
        defaultCommand = null
    }

    private val solenoid = DoubleSolenoid(0, 1)

    /**
     * Extends and retracts the hatch cylinder
     */
    fun setCylinder(extended: Boolean) {
        val value: DoubleSolenoid.Value = when (extended) {
            true -> DoubleSolenoid.Value.kForward
            false -> DoubleSolenoid.Value.kReverse
        }
        solenoid.set(value)
//        solenoidB.set(value)
    }

}