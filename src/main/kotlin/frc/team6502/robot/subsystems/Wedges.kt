package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.robot.RobotMap
import frc.team6502.robot.commands.endgame.DefaultWedges

object Wedges : Subsystem() {

    private val solenoidA = DoubleSolenoid(RobotMap.wedgeSolenoidIds[0], RobotMap.wedgeSolenoidIds[1])
    private val solenoidB = DoubleSolenoid(RobotMap.wedgeSolenoidIds[2], RobotMap.wedgeSolenoidIds[3])

    var deployed = false

    var unlock: Boolean
        get() = solenoidA.get() == DoubleSolenoid.Value.kForward
        set(value) {
            if (!value) deployed = true
            solenoidA.set(if (value) DoubleSolenoid.Value.kReverse else DoubleSolenoid.Value.kForward)
            solenoidB.set(if (value) DoubleSolenoid.Value.kForward else DoubleSolenoid.Value.kReverse)
        }

    override fun initDefaultCommand() {
        defaultCommand = DefaultWedges()
    }

}