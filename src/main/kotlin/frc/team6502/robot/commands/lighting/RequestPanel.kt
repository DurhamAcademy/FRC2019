/*package frc.team6502.robot.commands.lighting

import edu.wpi.first.wpilibj.command.TimedCommand
import frc.team6502.robot.LightingCommand
import frc.team6502.robot.subsystems.Lighting

class RequestPanel() : TimedCommand(2.0) {
    init {
        requires(Lighting)
    }

    override fun initialize() {
        Lighting.currentCommand = LightingCommand.PANEL
    }
}*/