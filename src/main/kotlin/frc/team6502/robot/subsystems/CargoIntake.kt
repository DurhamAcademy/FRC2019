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

    private val shooterTalon = TalonSRX(RobotMap.intakeTalonId)
    private val shooterVictor = VictorSPX(RobotMap.intakeVictorId)

    private val intakeTalon = TalonSRX(RobotMap.frontIntakeTalonId)

    init {
        // turn on brake mode and set directions for elevator wheels
        shooterTalon.setNeutralMode(NeutralMode.Brake)
        shooterTalon.inverted = true
        shooterVictor.setNeutralMode(NeutralMode.Brake)
        shooterVictor.follow(shooterTalon)
        shooterVictor.inverted = false
    }

    override fun initDefaultCommand() {
        defaultCommand = null
    }

    fun shooterDrection(reverse: Boolean) {
        if(reverse) {
            shooterTalon.inverted = false
            shooterVictor.inverted = true
        }
        else {
            shooterTalon.inverted = true
            shooterVictor.inverted = false
        }
    }
    /**
     * Roller speed, to be tested
     */
    //TODO
    var speedIntake: Double = 0.0
        set(value) {
            shooterTalon.set(ControlMode.PercentOutput, value * 0.5)
            intakeTalon.set(ControlMode.PercentOutput, value)
        }
    var speedShooter: Double = 0.0
        set(value) {
            shooterTalon.set(ControlMode.PercentOutput, value)
        }

    /**
     * Amount of shooterCurrent talons are drawing
     */
    val shooterCurrent: Double
        get() = shooterTalon.outputCurrent
    val intakeCurrent: Double
        get() = intakeTalon.outputCurrent
}