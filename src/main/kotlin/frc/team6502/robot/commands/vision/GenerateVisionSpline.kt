package frc.team6502.robot.commands.vision

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.stringify
import jaci.pathfinder.*

class GenerateVisionSpline() : InstantCommand() {

    companion object {
        var visionSpline: Trajectory? = null
    }

    override fun execute() {
        val cfg = Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, 0.05, 5.0, 2.0, 18.0)
        val waypoints = arrayOf(
                Waypoint(0.0, 0.0, Pathfinder.d2r(0.0)),
                Waypoint(CollectVisionData.avgY, CollectVisionData.avgX, Pathfinder.d2r(CollectVisionData.avgH))
        )
        val t = Pathfinder.generate(waypoints, cfg)
        visionSpline = t
        println("Generated trajectory in ${timeSinceInitialized() * 1000}ms")
        println("First ${t.segments.first().stringify()}")
        println("Last ${t.segments.last().stringify()}")
    }
}