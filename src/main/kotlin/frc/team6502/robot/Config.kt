package frc.team6502.robot

import frc.team6502.kyberlib.motorcontrol.PIDConfig
import frc.team6502.kyberlib.util.units.*

// Timings
const val TIMESTEP = 0.02
const val TIMESTEP_ODOMETRY = 0.02

// Auton
const val B = 3.0
const val ZETA = 0.8

// Elevator
val GROUND_DISTANCE = 6.inches.feet
val HEIGHTS = arrayListOf(
        0.inches to "Level 1",
        25.5.inches to "Level 2",
        51.inches to "Level 3"
)
val ELEVATOR_GAINS = PIDConfig(0.15, 0.0, 0.2)
val HOLD_VOLTAGE = 0.0

enum class CargoStatus(val heightOffset: Length) {
    NONE(0.inches),
    IDLE(2.5.inches),
    INTAKING_GROUND(0.inches),
    INTAKING_STATION(27.5.inches),
    ARMED_SHIP(28.inches),
    ARMED_ROCKET(12.inches)
}

enum class HatchStatus(val heightOffset: Length) {
    NONE(0.inches),
    ARMED(2.5.inches)
}

val CRUISE_UP = 15.feetPerSecond
val ACCEL_UP = 5.feetPerSecond // ^2
val CRUISE_DOWN = 2.feetPerSecond
val ACCEL_DOWN = 2.feetPerSecond // ^2

val DRIVETRAIN_MAXSPEED = 13.458.feetPerSecond
val DRIVETRAIN_WHEEL_RATIO = ((Math.PI * 6.0).inches.meters / 1.rotations.radians) / 0.9

// kV -> Volts per foot/sec
// kS -> Volts required to start moving
val kV_L = 0.654502
val kS_L = 1.064505

val kV_R = 0.632702
val kS_R = 0.995422

val quickStopThreshold = 0.2
val quickStopAlpha = 0.1