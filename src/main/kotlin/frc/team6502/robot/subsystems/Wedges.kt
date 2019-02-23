package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.robot.RobotMap

object Wedges : Subsystem() {

    private val solenoidA = Solenoid(RobotMap.wedgeSolenoidIds[0])
    private val solenoidB = Solenoid(RobotMap.wedgeSolenoidIds[1])

    var unlock
        get() = solenoidA.get()
        set(value) {
            solenoidA.set(value)
            solenoidB.set(value)
        }

    override fun initDefaultCommand() {
        defaultCommand = null
    }

}