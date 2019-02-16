package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.commandgroups.VisionAlign

/**
 * Global cancel operation. If you did something you didn't want to do, run this
 */
class CancelOperation : InstantCommand() {
    override fun execute() {
//        println("CANCEL CANCEL CANCEL CANCEL CANCEL")
        IntakeCargo.singleton?.cancel()
        VisionAlign.singleton?.cancel()
    }
}