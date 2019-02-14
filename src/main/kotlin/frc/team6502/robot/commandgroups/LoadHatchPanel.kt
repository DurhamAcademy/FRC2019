package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team6502.robot.commands.manip.SetHatchPanelExtended
import frc.team6502.robot.subsystems.HatchPanelIntake

class LoadHatchPanel() : CommandGroup() {
    companion object {
        private var hasPanel = false
    }

    init {
        requires(HatchPanelIntake)
        // down
//        addSequential(SetElevatorOffset)
        addSequential(WaitCommand(1.0))
        // out
        addSequential(SetHatchPanelExtended(true))

        // up
//        addSequential(SetElevatorOffset)

        // in
        addSequential(SetHatchPanelExtended(false))
        hasPanel = !hasPanel
    }

}