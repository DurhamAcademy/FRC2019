package frc.team6502.robot

import frc.team6502.kyberlib.motorcontrol.PIDConfig
import frc.team6502.kyberlib.util.units.feetPerSecond
import frc.team6502.kyberlib.util.units.inches

// Timings
const val TIMESTEP = 0.02
const val TIMESTEP_ODOMETRY = 0.01

// Auton
const val B = 0.5
const val ZETA = 0.5

// Elevator
val GROUND_DISTANCE = 6.inches.feet
val HEIGHTS = arrayListOf(
        8.inches to "Level 1",
        30.5.inches to "Level 2",
        58.5.inches to "Level 3"
)
val ELEVATOR_GAINS = PIDConfig(0.3, 0.0, 0.08)
val HOLD_VOLTAGE = 1.1

val CARGO_DELIVERY_OFFSET = 6.inches.feet
val CARGO_DELIVERY_L3_OFFSET = 4.inches.feet
val HATCH_DELIVERY_OFFSET = 12.inches.feet

val CRUISE_UP = 2.feetPerSecond
val ACCEL_UP = 1.feetPerSecond // ^2
val CRUISE_DOWN = 2.feetPerSecond
val ACCEL_DOWN = 1.feetPerSecond // ^2

