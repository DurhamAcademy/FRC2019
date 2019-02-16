package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commandgroups.LoadHatchPanel
import frc.team6502.robot.commandgroups.PlaceHatchPanel
import frc.team6502.robot.subsystems.HatchPanelIntake

class ManipulatePanel() : InstantCommand() {
    companion object {
        var hasPanel = false
    }

    init {
        requires(HatchPanelIntake)
    }

    override fun execute() {
        if (hasPanel) {
            PlaceHatchPanel().start()
        } else {
            LoadHatchPanel().start()
        }
        hasPanel = !hasPanel
        SmartDashboard.putBoolean("Has Panel", hasPanel)
    }
}