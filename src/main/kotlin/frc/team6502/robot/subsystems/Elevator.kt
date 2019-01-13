package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.kyberlib.motorcontrol.KTalonSRX

class Elevator: Subsystem() {

    companion object {
        val HEIGHT_ZERO = 0.0
        val HEIGHT_L1 = 1.0
        val HEIGHT_L2 = 2.0
        val HEIGHT_L3 = 3.0
    }

//    val elevatorTalon = KTalonSRX

    var height = 0.0
    set(value) {
        // elevatorTalon.position = height
    }

    override fun initDefaultCommand() {
        defaultCommand = null
    }

}