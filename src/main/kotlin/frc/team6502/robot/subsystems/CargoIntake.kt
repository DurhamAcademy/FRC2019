package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.robot.RobotMap

/**
 * Front roller and elevator wheels for cargo
 */
object CargoIntake : Subsystem() {

    private val intakeTalon = TalonSRX(RobotMap.intakeTalonId)
    private val intakeVictor = VictorSPX(RobotMap.intakeVictorId)

    private val frontIntakeTalon = TalonSRX(RobotMap.frontIntakeTalonId)

    init {
        // turn on brake mode and set directions for elevator wheels
        intakeTalon.setNeutralMode(NeutralMode.Brake)
        intakeTalon.inverted = true
        intakeVictor.setNeutralMode(NeutralMode.Brake)
        intakeVictor.follow(intakeTalon)
        intakeVictor.inverted = false
    }

    override fun initDefaultCommand() {
        defaultCommand = null
    }

    /**
     * Roller speed, to be tested
     */
    //TODO
    var speed: Double = 0.0
        set(value) {
            intakeTalon.set(ControlMode.PercentOutput, value * 0.5)
            frontIntakeTalon.set(ControlMode.PercentOutput, value)
        }

    /**
     * Amount of current talons are drawing
     */
    val current: Double
        get() = intakeTalon.outputCurrent

}