package frc.team6502.robot

import com.ctre.phoenix.sensors.PigeonIMU
import frc.team6502.kyberlib.util.units.*
import jaci.pathfinder.Pathfinder


fun PigeonIMU.getYaw() : Double {
    var ypr = doubleArrayOf(0.0, 0.0, 0.0)
    this.getYawPitchRoll(ypr)
    return ypr[0]
}

fun PigeonIMU.zero() {
    this.setYaw(0.0)
}

val Double.halfDegrees: Double
    get() = Pathfinder.boundHalfDegrees(this)

data class Pose(var x: Length, var y: Length, var theta: Angle)
data class Odometry(val pose: Pose, var velocity: LinearVelocity, var angularVelocity: AngularVelocity)