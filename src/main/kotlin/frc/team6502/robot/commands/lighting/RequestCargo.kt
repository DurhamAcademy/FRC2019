package frc.team6502.robot.commands.lighting

import edu.wpi.first.wpilibj.command.TimedCommand
import frc.team6502.robot.Lighting

class RequestCargo() : TimedCommand(2.0) {
    init {
        requires(Lighting)
    }

    override fun initialize() {
        Lighting.currentCommand = 2
    }

    override fun end() {
        Lighting.currentCommand = 0
    }
}