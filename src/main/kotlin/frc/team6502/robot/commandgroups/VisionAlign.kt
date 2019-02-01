package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import frc.team6502.robot.commands.RamseteFollowPath
import frc.team6502.robot.commands.vision.*
import frc.team6502.robot.subsystems.Drivetrain

object VisionAlign : CommandGroup() {
    init {
        requires(Drivetrain)
        addSequential(SetLEDRing(true))
        addSequential(CollectVisionData(1.0, 10))
        addSequential(SetLEDRing(false))
        addSequential(GenerateVisionSpline())
        addSequential(RamseteFollowPath(GenerateVisionSpline.visionSpline!!, 3.0, 0.6))
    }
}