package frc.team6502.robot.commands.vision

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.kyberlib.util.units.inches
import frc.team6502.robot.*
import frc.team6502.robot.commands.drive.RamseteFollowPath
import frc.team6502.robot.commands.manip.SetElevatorOffset
import jaci.pathfinder.*

/**
 * Generates a motion profile from the data collected from the vision system
 */
class GenerateVisionSpline() : InstantCommand() {

    companion object {
        var visionSpline: Trajectory? = null
    }

    override fun execute() {
        if (CollectVisionData.avgH == 0.0 && CollectVisionData.avgX == 0.0 && CollectVisionData.avgY == 0.0) {
            return
        }
        val cfg = Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, RobotMap.TIMESTEP, 5.0, 2.0, 18.0)
        val waypoints = arrayOf(
                Waypoint(0.0, 0.0, Pathfinder.d2r(0.0)),
                Waypoint(CollectVisionData.avgY - 11.inches.feet, -CollectVisionData.avgX - 13.inches.feet, Pathfinder.d2r(-CollectVisionData.avgH))
        )
        val t = Pathfinder.generate(waypoints, cfg)
        visionSpline = t
        println("Generated trajectory in ${timeSinceInitialized() * 1000}ms")
        println("First ${t.segments.first().stringify()}")
        println("Last ${t.segments.last().stringify()}")
        SetElevatorOffset(ElevatorOffset.HATCH_DELIVERY).start()
        RamseteFollowPath(t, 1.0, 0.6).start()
    }
}