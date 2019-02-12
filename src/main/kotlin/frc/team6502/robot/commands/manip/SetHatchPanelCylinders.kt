package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.subsystems.HatchPanelIntake

class SetHatchPanelCylinders(private val extended: Boolean) : InstantCommand() {
    init {
        requires(HatchPanelIntake)
    }

    override fun execute() {
        HatchPanelIntake.setCylinders(extended)
    }
}