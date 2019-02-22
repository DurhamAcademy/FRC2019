package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team6502.robot.ElevatorOffset
import frc.team6502.robot.commands.manip.SetElevatorOffset
import frc.team6502.robot.commands.manip.SetHatchPanelExtended
import frc.team6502.robot.subsystems.HatchPanelIntake

class LoadHatchPanel() : CommandGroup() {

    init {
        requires(HatchPanelIntake)

        // down
        addSequential(SetElevatorOffset(ElevatorOffset.HATCH_DELIVERY))
        addSequential(WaitCommand(1.0))
        // out
        addSequential(SetHatchPanelExtended(true))
        addSequential(WaitCommand(0.25))
        // up
        addSequential(SetElevatorOffset(ElevatorOffset.CARRY))
        addSequential(WaitCommand(1.0))
        // in
        addSequential(SetHatchPanelExtended(false))
    }

}