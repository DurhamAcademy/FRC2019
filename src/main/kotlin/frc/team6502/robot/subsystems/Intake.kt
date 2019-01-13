package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.command.Subsystem

class Intake: Subsystem() {
    override fun initDefaultCommand() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    var speed: Double = 0.0
    set(value) {
        // intakeTalon.percentVoltage = value
    }

//    val intakeTalon: KTalonSRX


}