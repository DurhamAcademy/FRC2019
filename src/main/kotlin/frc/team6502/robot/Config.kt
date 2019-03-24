package frc.team6502.robot

import frc.team6502.kyberlib.motorcontrol.PIDConfig
import frc.team6502.kyberlib.util.units.feetPerSecond
import frc.team6502.kyberlib.util.units.inches
import frc.team6502.kyberlib.util.units.rotations

// Timings
const val TIMESTEP = 0.02
const val TIMESTEP_ODOMETRY = 0.01

// Auton
const val B = 0.5
const val ZETA = 0.5

// Elevator
val GROUND_DISTANCE = 6.inches.feet
val HEIGHTS = arrayListOf(
        0.inches to "Level 1",
        25.5.inches to "Level 2",
        51.inches to "Level 3"
)
val ELEVATOR_GAINS = PIDConfig(0.5, 0.0, 0.08)
val HOLD_VOLTAGE = 1.1

val CARGO_DELIVERY_OFFSET = 12.inches.feet
val CARGO_DELIVERY_L3_OFFSET = 0.inches.feet
val HATCH_DELIVERY_OFFSET = 2.5.inches.feet

val LOADING_STATION_HEIGHT = 27.5.inches

val CRUISE_UP = 4.feetPerSecond
val ACCEL_UP = 2.feetPerSecond // ^2
val CRUISE_DOWN = 2.feetPerSecond
val ACCEL_DOWN = 1.feetPerSecond // ^2

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