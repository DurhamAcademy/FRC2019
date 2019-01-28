package frc.team6502.robot.commands

import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.command.Command
import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.*
import frc.team6502.robot.sensor.RobotOdometry
import frc.team6502.robot.subsystems.Drivetrain
import jaci.pathfinder.Trajectory
import java.lang.Math.cos
import java.lang.Math.sin
import kotlin.math.*

/**
 * Ramsete path follower
 * @property traj The trajectory to follow
 * @property b Ramsete tuning value (similar to P in PID)
 * @property zeta Ramsete tuning value (similar to D in PID)
 */
class RamseteFollowPath(private val traj: Trajectory, private val b: Double, private val zeta: Double) : Command() {

    private var currentIndex = 0
    private val drivebase = 3.feet

    override fun start() {
        println("Staring ramsete follow")
        RobotOdometry.zero()
        Notifier(this::nextSegment).startPeriodic(0.05)
    }

    private fun nextSegment() {
        println("Segment $currentIndex/${traj.segments.size}")

        val seg = traj.segments[currentIndex]
        val w = if (currentIndex > 0) (seg.heading - traj.segments[currentIndex - 1].heading) / 0.05 else 0.0

        val commanded = ramsete(RobotOdometry.odometry, Odometry(Pose(seg.x.feet, seg.y.feet, seg.heading.radians), seg.velocity.feetPerSecond, w.radiansPerSecond))
        Drivetrain.setDriveVelocities(commanded.first, commanded.second)
        RobotOdometry.addPose(RobotMap.kIMU.getYaw().degrees, Drivetrain.getVelocity())

        currentIndex++
    }

    private fun ramsete(odometry: Odometry, desired: Odometry): Pair<Double, Double> {
        val v = odometry.velocity.feetPerSecond
        val w = odometry.angularVelocity.radiansPerSecond
        val x = odometry.pose.x.feet
        val y = odometry.pose.y.feet
        val th = odometry.pose.theta.radians

        val vd = desired.velocity.feetPerSecond
        val wd = desired.angularVelocity.radiansPerSecond
        val xd = desired.pose.x.feet
        val yd = desired.pose.y.feet
        val thd = desired.pose.theta.radians

        val k1 = k13gains(desired.velocity.feetPerSecond, desired.angularVelocity.radiansPerSecond)
        val k2 = b * vd.absoluteValue

        val vc = vd * cos(thd - th) + k1 * ((xd - x) * cos(th) + (yd - y) * sin(th))
        val wc = wd + k2 * vd * sinc(th, thd) * ((yd - y) * cos(th) - (xd - x) * sin(th)) + k1 * (thd - th)

        val difference = wc.radiansPerSecond.toLinearVelocity((PI * 4.0.feet.meters) / 1.rotations.radians)

        //TODO: revisit kinematic calculations here
        return vc + difference.feetPerSecond to vc - difference.feetPerSecond
    }

    fun k13gains(vd: Double, wd: Double) = 2 * zeta * sqrt(wd.pow(2) + b * vd.pow(2))
    fun sinc(theta: Double, thetad: Double) = sin(thetad - theta) / (thetad - theta)

    override fun isFinished() = currentIndex == traj.segments.size

}