package frc.team6502.robot.sensor

import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.Odometry
import frc.team6502.robot.Pose
import frc.team6502.robot.RobotMap
import frc.team6502.robot.zero
import kotlin.math.cos
import kotlin.math.sin

object RobotOdometry {

    val odometry = Odometry(Pose(0.feet, 0.feet, 0.radians), 0.feetPerSecond, 0.radiansPerSecond)

    fun addPose(theta: Angle, vel: LinearVelocity) {
        odometry.angularVelocity = ((theta.radians - odometry.pose.theta.radians) / 0.05).radiansPerSecond
        odometry.pose.theta = theta
        odometry.pose.x += (cos(theta.radians) * vel.feetPerSecond * 0.05).feet
        odometry.pose.y += (sin(theta.radians) * vel.feetPerSecond * 0.05).feet
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