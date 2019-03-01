package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team6502.robot.GamePiece
import frc.team6502.robot.commands.manip.SetGamePiece
import frc.team6502.robot.commands.manip.SetHatchPanelExtended
import frc.team6502.robot.subsystems.HatchPanelIntake

class LoadHatchPanel() : CommandGroup() {

    init {
        requires(HatchPanelIntake)

        // out
        addSequential(SetHatchPanelExtended(true))
        addSequential(WaitCommand(0.25))
        // up
        addSequential(SetGamePiece(GamePiece.HATCH))
        addSequential(WaitCommand(1.0))
        // in
        addSequential(SetHatchPanelExtended(false))
    }

}