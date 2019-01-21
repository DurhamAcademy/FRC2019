package frc.team6502.robot

import com.ctre.phoenix.sensors.PigeonIMU
import edu.wpi.first.wpilibj.PIDOutput
import edu.wpi.first.wpilibj.PIDSource
import edu.wpi.first.wpilibj.PIDSourceType
import frc.team6502.kyberlib.util.units.Angle
import frc.team6502.kyberlib.util.units.AngularVelocity
import frc.team6502.kyberlib.util.units.Length
import frc.team6502.kyberlib.util.units.LinearVelocity
import jaci.pathfinder.Pathfinder


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

class ArbitraryPIDSource(val type: PIDSourceType, val lambda: () -> Double) : PIDSource {
    override fun getPIDSourceType() = type

    override fun setPIDSourceType(pidSource: PIDSourceType?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun pidGet(): Double = lambda()
}

class ArbitraryPIDOutput() : PIDOutput {

    var output = 0.0
    override fun pidWrite(output: Double) {
        this.output = output
    }

}

data class Pose(var x: Length, var y: Length, var theta: Angle)
data class Odometry(val pose: Pose, var velocity: LinearVelocity, var angularVelocity: AngularVelocity)