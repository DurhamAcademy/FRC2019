package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team6502.robot.commands.endgame.SetWedgesLocked
import frc.team6502.robot.subsystems.Wedges

class DeployWedges() : CommandGroup() {
    init {
        requires(Wedges)
        addSequential(SetWedgesLocked(false))
        addSequential(WaitCommand(5.0))
        addSequential(SetWedgesLocked(true))
    }
}