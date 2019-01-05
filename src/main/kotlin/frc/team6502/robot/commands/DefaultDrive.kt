package frc.team6502.robot.commands

import edu.wpi.first.wpilibj.command.Command
import frc.team6502.robot.RobotMap

class DefaultDrive : Command() {

    init {
        requires(RobotMap.kDrivetrain)
    }

    override fun initialize() {
        println("Drive initialized :)")
    }

    override fun execute() {
        println("spam")
    }

    //TODO("not implemented")

    override fun isFinished(): Boolean {
        return false
    }

}