package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.robot.LightingCommand
import frc.team6502.robot.commands.lighting.ChaseAllianceColor



object Lighting : Subsystem() {

    override fun initDefaultCommand() {
        defaultCommand = ChaseAllianceColor()
    }

    private val leds = I2C(I2C.Port.kOnboard, 0x02)
    private val bytes = ByteArray(1)


    var currentCommand = LightingCommand.OFF

    override fun periodic() {
        bytes[0] = currentCommand.cmd.toByte()
        leds.writeBulk(bytes)
    }
}