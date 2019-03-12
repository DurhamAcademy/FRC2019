package frc.team6502.robot.commands.endgame

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.subsystems.Wedges

class SetWedgesLocked(val locked: Boolean) : InstantCommand() {
    init {
//        requires(Wedges)
    }

    override fun execute() {
        Wedges.unlock = !locked
    }
}