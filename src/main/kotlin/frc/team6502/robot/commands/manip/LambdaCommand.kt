package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand

class LambdaCommand(val l:()->Unit): InstantCommand() {
    override fun execute() {
        l()
    }
}