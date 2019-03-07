package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.Solenoid
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.robot.RobotMap
import frc.team6502.robot.commands.endgame.DefaultWedges

object Wedges : Subsystem() {

    private val solenoidA = Solenoid(RobotMap.wedgeSolenoidIds[0])
    private val solenoidB = Solenoid(RobotMap.wedgeSolenoidIds[1])

    var deployed = false

    var unlock
        get() = solenoidA.get()
        set(value) {
            if (!value) deployed = true
            solenoidA.set(value)
            solenoidB.set(value)
        }

    override fun initDefaultCommand() {
        defaultCommand = DefaultWedges()
    }

}