package frc.team6502.robot.commands.drive

import edu.wpi.first.wpilibj.command.Command
import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.*
import frc.team6502.robot.sensor.RobotOdometry
import frc.team6502.robot.subsystems.Drivetrain
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import java.io.File
import java.lang.Math.sin
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Ramsete path follower
 * @property traj The trajectory to follow
 * @property b Ramsete tuning value (similar to P in PID)
 * @property zeta Ramsete tuning value (similar to D in PID)
 */
class RamseteFollowPath(private val traj: Trajectory, private val b: Double, private val zeta: Double) : Command() {

    private var currentIndex = 0
    private val drivebase = 29.inches
    private val logFile = File("/U/ramsetelog_${System.currentTimeMillis()}.csv")

    init {
        requires(Drivetrain)
    }

    override fun initialize() {
        println("Staring ramsete follow")
        logFile.writeText("t, vel_a, vel_d, avel_a, avel_d, x_a, x_d, y_a, y_d, th_a, th_d, k1k3, k2, vel_c, avel_c, x_e, y_e, th_e\n")
        RobotOdometry.zero()
    }

    override fun execute() {
        println("Segment $currentIndex/${traj.segments.size}")

        val seg = traj.segments[currentIndex]
        val w = if (currentIndex > 0) {
            (boundHalf(seg.heading - traj.segments[currentIndex - 1].heading)) / TIMESTEP
        } else {
            0.0
        }
        println(w)
        val commanded = ramsete(
                RobotOdometry.odometry,
                Odometry(Pose(seg.x.feet, seg.y.feet, boundHalf(seg.heading).radians), seg.velocity.feetPerSecond, w.radiansPerSecond),
                false
        )

        Drivetrain.set(
                (commanded.first / DRIVETRAIN_MAXSPEED.feetPerSecond).coerceIn(-1.0, 1.0),
                (commanded.second / DRIVETRAIN_MAXSPEED.feetPerSecond).coerceIn(-1.0, 1.0),
                DrivetrainMode.CLOSED_LOOP)

        currentIndex++
    }

    private fun ramsete(odometry: Odometry, desired: Odometry, disableTurn: Boolean): Pair<Double, Double> {

        // actual
        val v = odometry.velocity.feetPerSecond
        val w = odometry.angularVelocity.radiansPerSecond
        val x = odometry.pose.x.feet
        val y = odometry.pose.y.feet
        val th = boundHalf(odometry.pose.theta.radians)

        // desired
        val vd = desired.velocity.feetPerSecond
        val wd = desired.angularVelocity.radiansPerSecond
        val xd = desired.pose.x.feet
        val yd = desired.pose.y.feet
        val thd = boundHalf(desired.pose.theta.radians)

        val k1 = k13gains(desired.velocity.feetPerSecond, desired.angularVelocity.radiansPerSecond)
        val k2 = b * vd.absoluteValue

//        println(thd - th)
        val vc = vd * cos(thd - th) + k1 * ((xd - x) * cos(th) + (yd - y) * sin(th))
        var wc = wd + k2 * vd * sinc(th, thd) * ((yd - y) * cos(th) - (xd - x) * sin(th)) + k1 * (thd - th)
        logFile.appendText("$currentIndex, $v, $vd, $w, $wd, $x, $xd, $y, $yd, $th, $thd, $k1, $k2, $vc, $wc, ${xd - x}, ${yd - y}, ${thd - th}\n")
//        val difference = 0.feetPerSecond
//        val difference = wc.radiansPerSecond.toLinearVelocity((PI * drivebase.meters) / 1.rotations.radians)
        return vc - wc * drivebase.feet / 2.0 to vc + wc * drivebase.feet / 2.0
    }

    fun k13gains(vd: Double, wd: Double) = 2 * zeta * sqrt(wd.pow(2) + b * vd.pow(2))
    fun sinc(theta: Double, thetad: Double): Double {
        return if ((thetad - theta).absoluteValue > 0.01) sin(thetad - theta) / (thetad - theta) else 1.0
    }

    override fun isFinished(): Boolean {
//        println("checked")
        return currentIndex == traj.segments.size
    }

    override fun end() {
//        println("done")
        Drivetrain.set(
                0.0,
                0.0,
                DrivetrainMode.CLOSED_LOOP)
    }

    fun boundHalf(ang: Double): Double {
        return Pathfinder.boundHalfDegrees(ang.radians.degrees).degrees.radians
    }

}