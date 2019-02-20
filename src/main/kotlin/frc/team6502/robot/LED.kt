package frc.team6502.robot

import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

object LED {
    private val leds = I2C(I2C.Port.kOnboard, 0x02)
    private val bytes = ByteArray(1)
    private val Timer = Timer()
    private var currentCommand = 0

    fun execute() {
        bytes[0] = 0
        if(SmartDashboard.getBoolean("Request Hatch", false) || (currentCommand == 1 && Timer.get() < 2))  {
            currentCommand = 1
            bytes[0] = 1
            Timer.start()
        }
        else if(SmartDashboard.getBoolean("Request Cargo", false) || (currentCommand == 2 && Timer.get() < 2)) {
            currentCommand = 2
            bytes[0] = 2
            Timer.start()
        }
        else {
            currentCommand = 0
            Timer.stop()
            Timer.reset()
        }
        leds.writeBulk(bytes)
    }
}