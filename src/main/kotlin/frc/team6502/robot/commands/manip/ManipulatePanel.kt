package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commandgroups.LoadHatchPanel
import frc.team6502.robot.commandgroups.PlaceHatchPanel

class ManipulatePanel() : InstantCommand() {
    companion object {
        var hasPanel = false
    }

    init {


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