package frc.team6502.robot

import edu.wpi.first.wpilibj.I2C

val leds = I2C(I2C.Port.kOnboard, 0)

class LED {
    fun execute() {
        leds.write(0, 2)
    }
}