package frc.team6502.robot.sensor

import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.*
import frc.team6502.robot.subsystems.Drivetrain
import kotlin.math.cos
import kotlin.math.sin

object RobotOdometry {

    val odometry = Odometry(Pose(0.feet, 0.feet, 0.radians), 0.feetPerSecond, 0.radiansPerSecond)

    fun update() {
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