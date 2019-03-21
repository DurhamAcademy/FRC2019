package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.TalonSRX
import com.ctre.phoenix.motorcontrol.can.VictorSPX
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.robot.RobotMap

/**
 * Front roller and elevator wheels for cargo
 */
object CargoIntake : Subsystem() {

    private val shooterTalon = TalonSRX(RobotMap.intakeTalonId)
    private val shooterVictor = VictorSPX(RobotMap.intakeVictorId)

    private val intakeTalon = TalonSRX(RobotMap.frontIntakeTalonId)
    private val rampSolenoid = DoubleSolenoid(RobotMap.rampSolenoidIds[0], RobotMap.rampSolenoidIds[1])

    init {
        // turn on brake mode and set directions for elevator wheels
        shooterTalon.setNeutralMode(NeutralMode.Brake)
        shooterTalon.inverted = true
        shooterVictor.setNeutralMode(NeutralMode.Brake)
        shooterVictor.follow(shooterTalon)
        shooterVictor.inverted = false

        intakeTalon.configContinuousCurrentLimit(12)
        intakeTalon.setNeutralMode(NeutralMode.Brake)

        intakeTalon.inverted = true

        rampSolenoid.set(DoubleSolenoid.Value.kReverse)
    }

    override fun initDefaultCommand() {
        defaultCommand = null
    }

    var speedIntake: Double = 0.0
        set(value) {
            intakeTalon.set(ControlMode.PercentOutput, -value)
            field = value
        }

    var speedShooter: Double = 0.0
        set(value) {
            shooterTalon.set(ControlMode.PercentOutput, value)
            field = value
        }

    /**
     * Amount of shooterCurrent talons are drawing
     */
    val shooterCurrent: Double
        get() = shooterTalon.outputCurrent
    val intakeCurrent: Double
        get() = intakeTalon.outputCurrent

    var rampState
        get() = rampSolenoid.get() == DoubleSolenoid.Value.kForward
        set(value){
            rampSolenoid.set(if(value){
                DoubleSolenoid.Value.kForward
            } else {
                DoubleSolenoid.Value.kReverse
            })
        }
}