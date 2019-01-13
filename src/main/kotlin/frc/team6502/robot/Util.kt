package frc.team6502.robot

import com.ctre.phoenix.sensors.PigeonIMU


fun PigeonIMU.getYaw() : Double {
    var ypr = doubleArrayOf(0.0, 0.0, 0.0)
    this.getYawPitchRoll(ypr)
    return ypr[0]
}

fun PigeonIMU.zero() {
    this.setYaw(0.0)
}