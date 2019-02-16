package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.subsystems.HatchPanelIntake

class SetHatchPanelExtended(val extended: Boolean) : InstantCommand() {

    init {
        requires(HatchPanelIntake)
    }

    override fun execute() {
        HatchPanelIntake.setCylinder(extended)
    }
}