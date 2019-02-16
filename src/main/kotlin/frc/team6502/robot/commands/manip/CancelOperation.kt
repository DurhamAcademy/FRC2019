package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand

class CancelOperation : InstantCommand() {
    override fun execute() {
        println("CANCEL CANCEL CANCEL CANCEL CANCEL")
        IntakeCargo.singleton?.cancel()
    }
}