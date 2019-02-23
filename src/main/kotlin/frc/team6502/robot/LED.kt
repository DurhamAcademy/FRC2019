package frc.team6502.robot

import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.Notifier
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

object LED {
    private val leds = I2C(I2C.Port.kOnboard, 0x02)
    private val bytes = ByteArray(1)
    private val timer = Timer()
    private var currentCommand = 0
    private val notif = Notifier { update() }

    init {
        notif.startPeriodic(0.02)
    }

    private fun update() {
        bytes[0] = 0
        if (SmartDashboard.getBoolean("Request Hatch", false) || (currentCommand == 1 && timer.get() < 2)) {
            SmartDashboard.putBoolean("Request Hatch", false)
            currentCommand = 1
            bytes[0] = 1
            timer.start()
        } else if (SmartDashboard.getBoolean("Request Cargo", false) || (currentCommand == 2 && timer.get() < 2)) {
            SmartDashboard.putBoolean("Request Cargo", false)
            currentCommand = 2
            bytes[0] = 2
            timer.start()
        }
        else {
            currentCommand = 0
            timer.stop()
            timer.reset()
        }
        leds.writeBulk(bytes)
    }
}