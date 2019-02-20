package frc.team6502.robot

import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.Timer
import frc.team6502.robot.subsystems.Elevator

object LED {
    private val leds = I2C(I2C.Port.kOnboard, 0x02)
    private val bytes = ByteArray(2)
    private val Timer = Timer()
    /*
    1: Command
    2: Modifier (elevator position)
     */
    fun execute() {
        bytes[0] = 0
        if(Elevator.height > 0.5) {
            bytes[0] = 1
            bytes[1] = Elevator.height.toByte()
        }
        else {
            bytes[1] = 0
        }
        leds.writeBulk(bytes)
    }
}