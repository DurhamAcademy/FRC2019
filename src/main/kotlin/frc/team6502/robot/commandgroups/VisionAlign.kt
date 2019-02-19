package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team6502.robot.commands.vision.*
import frc.team6502.robot.subsystems.Drivetrain

class VisionAlign : CommandGroup() {
    companion object {
        var singleton: CommandGroup? = null
    }
    init {
        requires(Drivetrain)
        addSequential(SetLEDRing(true))
        addSequential(WaitCommand(0.5))
        addSequential(CollectVisionData(1.0, 10))
        addSequential(SetLEDRing(false))
        addSequential(GenerateVisionSpline())
//        addSequential(RamseteFollowPath(1.0, 0.6))
    }

    override fun initialize() {
        singleton = this
    }

    override fun end() {
        singleton = null
    }

    override fun interrupted() {
        singleton = null
    }
}