package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team6502.robot.*
import frc.team6502.robot.commands.manip.LambdaCommand
import frc.team6502.robot.commands.manip.SetHatchPanelExtended
import frc.team6502.robot.subsystems.Elevator
import frc.team6502.robot.subsystems.HatchPanelIntake

class PlaceHatchPanel() : CommandGroup() {

    init {
        requires(HatchPanelIntake)

        // out
        addSequential(SetHatchPanelExtended(true))
        addSequential(WaitCommand(0.5))

        // down
        addSequential(LambdaCommand { RobotStatus.setStatusHatch(HatchStatus.NONE) })
        addSequential(WaitForCommand({ Elevator.elevatorTalon.closedLoopError < 256 }, 5.0))

        // in
        addSequential(SetHatchPanelExtended(false))
    }

}