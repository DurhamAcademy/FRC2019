package frc.team6502.robot

import com.ctre.phoenix.sensors.PigeonIMU
import frc.team6502.kyberlib.util.units.Angle
import frc.team6502.kyberlib.util.units.AngularVelocity
import frc.team6502.kyberlib.util.units.Length
import frc.team6502.kyberlib.util.units.LinearVelocity
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory


fun PigeonIMU.getYaw() : Double {
    var ypr = doubleArrayOf(0.0, 0.0, 0.0)
    this.getYawPitchRoll(ypr)
    return ypr[0]
}

fun PigeonIMU.getPitch(): Double {
    var ypr = doubleArrayOf(0.0, 0.0, 0.0)
    this.getYawPitchRoll(ypr)
    return ypr[1]
}

fun PigeonIMU.zero() {
    this.setYaw(0.0)
}

val Double.halfDegrees: Double
    get() = Pathfinder.boundHalfDegrees(this)

fun Trajectory.Segment.stringify() {
    "Segment @ x=${this.x} y=${this.y} h=${this.heading}"
}

data class Pose(var x: Length, var y: Length, var theta: Angle)
data class Odometry(val pose: Pose, var velocity: LinearVelocity, var angularVelocity: AngularVelocity)

enum class ElevatorOffset {
    HATCH_DELIVERY,
    CARGO_DELIVERY,
    CARRY
}