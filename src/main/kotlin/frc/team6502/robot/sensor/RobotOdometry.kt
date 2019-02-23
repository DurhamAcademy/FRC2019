package frc.team6502.robot.sensor

import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.SendableBase
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.*
import frc.team6502.robot.subsystems.Drivetrain
import kotlin.math.cos
import kotlin.math.sin

object RobotOdometry : SendableBase() {

    override fun initSendable(builder: SendableBuilder?) {
        builder?.addDoubleProperty("x", { odometry.pose.x.feet }, null)
        builder?.addDoubleProperty("y", { odometry.pose.y.feet }, null)
        builder?.addDoubleProperty("h", { odometry.pose.theta.degrees }, null)
        builder?.addDoubleProperty("v", { odometry.velocity.feetPerSecond }, null)
        builder?.addDoubleProperty("w", { odometry.angularVelocity.radiansPerSecond }, null)
    }

    private val odometryNotifier = Notifier { update() }

    init {
        odometryNotifier.startPeriodic(TIMESTEP_ODOMETRY)
        SmartDashboard.putData(this)
    }
    val odometry = Odometry(Pose(0.feet, 0.feet, 0.radians), 0.feetPerSecond, 0.radiansPerSecond)


    private fun update() {
        addPose(RobotMap.kIMU.getYaw().degrees, (Drivetrain.getVelocities().first + Drivetrain.getVelocities().second) / 2.0)
    }

    private fun addPose(theta: Angle, vel: LinearVelocity) {
        odometry.velocity = vel
        odometry.angularVelocity = ((theta.radians - odometry.pose.theta.radians) / TIMESTEP_ODOMETRY).radiansPerSecond
        odometry.pose.theta = theta
        odometry.pose.x += (cos(theta.radians) * vel.feetPerSecond * TIMESTEP_ODOMETRY).feet
        odometry.pose.y += (sin(theta.radians) * vel.feetPerSecond * TIMESTEP_ODOMETRY).feet
    }

    fun zero() {
        RobotMap.kIMU.zero()
        odometry.pose.x = 0.feet
        odometry.pose.y = 0.feet
        odometry.pose.theta = 0.radians
        odometry.velocity = 0.feetPerSecond
        odometry.angularVelocity = 0.radiansPerSecond
    }
}