package frc.team6502.robot.commands.vision

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.kyberlib.util.units.inches
import frc.team6502.robot.*
import frc.team6502.robot.commands.drive.RamseteFollowPath
import frc.team6502.robot.commands.manip.SetElevatorOffset
import jaci.pathfinder.*
import java.io.File

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
        val cfg = Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_LOW, RobotMap.TIMESTEP, 5.0, 2.0, 18.0)

        val targetX = CollectVisionData.avgY - 5.inches.feet
        val targetY = -CollectVisionData.avgX - 13.inches.feet
        val targetA = 0.0//-CollectVisionData.avgH

        println("DESIRED X: $targetX, DESIRED Y: $targetY, DESIRED A: $targetA")

        val straightDistance = 1.0


        val waypoints = arrayOf(
                Waypoint(0.0, 0.0, Pathfinder.d2r(0.0)),
                /*Waypoint(targetX - straightDistance * cos(targetA),
                        targetY - straightDistance * sin(targetA),
                        Pathfinder.d2r(targetA)),*/
                Waypoint(targetX, targetY, Pathfinder.d2r(targetA))
        )
        val t = Pathfinder.generate(waypoints, cfg)
        if (t.segments.isEmpty()) return
        Pathfinder.writeToCSV(File("/U/visionspline_${System.currentTimeMillis()}.csv"), t)
        visionSpline = t
        println("Generated trajectory in ${timeSinceInitialized() * 1000}ms")
        println("First ${t.segments.first().stringify()}")
        println("Last ${t.segments.last().stringify()}")
        SetElevatorOffset(ElevatorOffset.HATCH_DELIVERY).start()
        RamseteFollowPath(t, 0.5, 0.5).start()
    }
}