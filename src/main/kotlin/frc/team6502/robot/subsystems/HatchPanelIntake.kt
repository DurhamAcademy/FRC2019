package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.robot.RobotMap

object HatchPanelIntake : Subsystem() {
    override fun initDefaultCommand() {
        defaultCommand = null
    }

    private val solenoid = Solenoid(RobotMap.hatchSolenoidId)

    /**
     * Extends and retracts the hatch cylinder
     */
    fun setCylinder(extended: Boolean) {
        solenoid.set(extended)
//        solenoidB.set(value)
    }

}