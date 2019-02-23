package frc.team6502.robot

import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.command.Subsystem

object Lighting : Subsystem() {
    override fun initDefaultCommand() {
        defaultCommand = null
    }

    private val leds = I2C(I2C.Port.kOnboard, 0x02)
    private val bytes = ByteArray(1)

    var currentCommand = 0

    override fun periodic() {
        bytes[0] = currentCommand.toByte()
        leds.writeBulk(bytes)
    }
}