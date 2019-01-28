package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.robot.RobotMap
import frc.team6502.robot.commands.DefaultIntake

object Intake : Subsystem() {

    private val intakeVictor = VictorSPX(RobotMap.intakeVictorID)

    init {
        intakeVictor.setNeutralMode(NeutralMode.Brake)
    }

    override fun initDefaultCommand() {
        defaultCommand = DefaultIntake()
    }

    var speed: Double = 0.0
    set(value) {
        intakeVictor.set(ControlMode.PercentOutput, value)
    }

}