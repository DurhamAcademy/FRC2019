package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import frc.team6502.robot.commands.vision.CollectVisionData
import frc.team6502.robot.commands.vision.GenerateVisionSpline
import frc.team6502.robot.subsystems.Drivetrain

class VisionAlign : CommandGroup() {
    init {
        requires(Drivetrain)
//        setElevatorUp
//        addSequential(SetLEDRing(true))
        addSequential(CollectVisionData())
//        addSequential(SetLEDRing(false))
        addSequential(GenerateVisionSpline())
//        addSequential(RamseteFollowPath(traj, 3, 0.6))
//        set elevator down if needed
    }
}