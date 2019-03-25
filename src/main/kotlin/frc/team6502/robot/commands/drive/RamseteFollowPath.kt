package frc.team6502.robot.commands.drive

import edu.wpi.first.wpilibj.PIDController
import edu.wpi.first.wpilibj.command.Command
import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.*
import frc.team6502.robot.sensor.RobotOdometry
import frc.team6502.robot.sensor.VisionPIDSource
import frc.team6502.robot.subsystems.Drivetrain
import jaci.pathfinder.*
import java.io.File
import java.lang.Math.sin
import kotlin.math.*

/**
 * Ramsete path follower
 * @property traj The trajectory to follow
 * @property b Ramsete tuning value (similar to P in PID)
 * @property zeta Ramsete tuning value (similar to D in PID)
 */
class RamseteFollowPath(private val traj: Trajectory, private val b: Double, private val zeta: Double, private val visionSeconds: Double = 0.0) : Command() {

    /**
     * Follows a path read from a file in /home/lvuser/deploy/paths (src/main/deploy/paths on local codebase)
     * @param name the name of the path to follow
     * @param b Ramsete tuning value (similar to P in PID)
     * @param zeta Ramsete tuning value (similar to D in PID)
     */
    constructor(name: String, b: Double, zeta: Double, visionSeconds: Double = 0.0) : this(PathfinderFRC.getTrajectory(name), b, zeta, visionSeconds)

    private var currentIndex = 0
    private val drivebase = 29.inches
    private val logFile = File("/home/lvuser/logs/ramsetelog_${System.currentTimeMillis()}.csv")
    private val visionSamples = visionSeconds / TIMESTEP
    private var visionCorrection = 0.0
    private val visionPID = PIDController(0.01, 0.0, 0.01, VisionPIDSource()) {
        visionCorrection = it.coerceIn(-0.33..0.33)
    }

    init {
        requires(Drivetrain)
        for (t in traj.segments.indices) {
            traj[t].y = 27 - traj[t].y
        }
        val xOffset = traj[0].x
        val yOffset = traj[0].y
        visionPID.enable()
        for (t in traj.segments.indices) {
            traj[t].x -= xOffset
            traj[t].y -= yOffset
        }
    }

    override fun initialize() {
        println("Starting ramsete follow")
        if(!logFile.exists()) logFile.createNewFile()
        logFile.writeText("t, vel_a, vel_d, avel_a, avel_d, x_a, x_d, y_a, y_d, th_a, th_d, k1k3, k2, vel_c, avel_c, x_e, y_e, th_e, vision\n")
        RobotOdometry.zero()
    }

    override fun execute() {
//        println("Segment $currentIndex/${traj.segments.size}")

        val seg = traj.segments[currentIndex]
        val w = if (currentIndex > 0) {
            (boundHalf(seg.heading - traj.segments[currentIndex - 1].heading)) / TIMESTEP
        } else {
            0.0
        }

        val commanded = ramsete(
                RobotOdometry.odometry,
                Odometry(Pose(seg.x.feet, seg.y.feet, boundHalf(seg.heading).radians), seg.velocity.feetPerSecond, w.radiansPerSecond),
                traj.segments.size - currentIndex < visionSamples
        )

        Drivetrain.set(
                (commanded.first / DRIVETRAIN_MAXSPEED.feetPerSecond).coerceIn(-1.0, 1.0),
                (commanded.second / DRIVETRAIN_MAXSPEED.feetPerSecond).coerceIn(-1.0, 1.0),
                DrivetrainMode.CLOSED_LOOP)

        currentIndex++
    }

    private fun ramsete(odometry: Odometry, desired: Odometry, useVision: Boolean): Pair<Double, Double> {

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

        val wc = if (!useVision) wd + k2 * vd * sinc(th, thd) * ((yd - y) * cos(th) - (xd - x) * sin(th)) + k1 * (thd - th) else 0.0
        val corr = if (!useVision) 0.0 else visionCorrection
        logFile.appendText("$currentIndex, $v, $vd, $w, $wd, $x, $xd, $y, $yd, $th, $thd, $k1, $k2, $vc, $wc, ${xd - x}, ${yd - y}, ${thd - th}, $useVision\n")
//        val difference = 0.feetPerSecond
//        val difference = wc.radiansPerSecond.toLinearVelocity((PI * drivebase.meters) / 1.rotations.radians)
        return vc - corr - wc * drivebase.feet / 2.0 to vc + corr + wc * drivebase.feet / 2.0
    }

    private fun k13gains(vd: Double, wd: Double) = 2 * zeta * sqrt(wd.pow(2) + b * vd.pow(2))
    private fun sinc(theta: Double, thetad: Double): Double {
        return if ((thetad - theta).absoluteValue > 0.01) sin(thetad - theta) / (thetad - theta) else 1.0
    }

    override fun isFinished(): Boolean {
//        println("checked")
        return currentIndex == traj.segments.size || OI.controller.aButton
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