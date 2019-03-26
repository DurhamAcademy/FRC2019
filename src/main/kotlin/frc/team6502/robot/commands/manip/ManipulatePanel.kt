package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.CargoStatus
import frc.team6502.robot.GamePiece
import frc.team6502.robot.HatchStatus
import frc.team6502.robot.RobotStatus
import frc.team6502.robot.commandgroups.LoadHatchPanel
import frc.team6502.robot.commandgroups.PlaceHatchPanel
import frc.team6502.robot.subsystems.HatchPanelIntake

class ManipulatePanel() : InstantCommand() {

    init {
        requires(HatchPanelIntake)

    }

    override fun execute() {
        if(RobotStatus.cargoStatus != CargoStatus.NONE) return
        when (RobotStatus.hatchStatus){
            HatchStatus.ARMED -> PlaceHatchPanel().start()
            HatchStatus.NONE -> LoadHatchPanel().start()
        }
    }
}