/*package frc.team6502.robot.commands.lighting

import edu.wpi.first.wpilibj.DriverStation
import edu.wpi.first.wpilibj.command.Command
import frc.team6502.robot.LightingCommand
import frc.team6502.robot.subsystems.Lighting

class ChaseAllianceColor() : Command() {

    init {
        requires(Lighting)
        setRunWhenDisabled(true)
    }

    override fun execute() {
        Lighting.currentCommand = when (DriverStation.getInstance().alliance) {
            DriverStation.Alliance.Red -> LightingCommand.CHASE_RED
            DriverStation.Alliance.Blue -> LightingCommand.CHASE_BLUE
            else -> LightingCommand.OFF
        }

    }

    override fun isFinished(): Boolean {
        return false
    }

}*/