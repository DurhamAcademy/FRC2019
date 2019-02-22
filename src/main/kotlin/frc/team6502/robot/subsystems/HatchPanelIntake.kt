package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj.command.Subsystem

object HatchPanelIntake : Subsystem() {
    override fun initDefaultCommand() {
        defaultCommand = null
    }

    private val solenoid = Solenoid(0)

    /**
     * Extends and retracts the hatch cylinder
     */
    fun setCylinder(extended: Boolean) {
        solenoid.set(extended)
//        solenoidB.set(value)
    }

}