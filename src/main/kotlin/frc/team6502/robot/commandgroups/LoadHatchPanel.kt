package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team6502.robot.HatchStatus
import frc.team6502.robot.RobotStatus
import frc.team6502.robot.commands.manip.LambdaCommand
import frc.team6502.robot.commands.manip.SetHatchPanelExtended
import frc.team6502.robot.subsystems.HatchPanelIntake

class LoadHatchPanel() : CommandGroup() {

    init {
        requires(HatchPanelIntake)

        // out
        addSequential(SetHatchPanelExtended(true))
        addSequential(WaitCommand(0.25))
        // up
        addSequential(LambdaCommand { RobotStatus.setStatusHatch(HatchStatus.ARMED)})
        addSequential(WaitCommand(0.5))
        // in
        addSequential(SetHatchPanelExtended(false))
    }

}