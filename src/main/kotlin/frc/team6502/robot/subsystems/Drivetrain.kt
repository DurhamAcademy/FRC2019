package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.*
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.DrivetrainMode
import frc.team6502.robot.RobotMap
import frc.team6502.robot.commands.defaults.DefaultDrive
import kotlin.math.sign

object Drivetrain :  Subsystem() {

    private val leftTalon = WPI_TalonSRX(RobotMap.leftTalonId)
    private val rightTalon = WPI_TalonSRX(RobotMap.rightTalonId)

    val maxSpeed = 13.30.feetPerSecond
    val wheelRatio = (Math.PI * 6.0).inches.meters / 1.rotations.radians

    private val kV = .80482
    private val vI = 0.11607

    init {
        // probably going to be a wcd
        // two ktalon objects needed, config tbd
        for(id in RobotMap.leftVictorIds){
            WPI_VictorSPX(id).run {
                follow(leftTalon)
                inverted = true
            }
        }

        for (id in RobotMap.rightVictorIds) {
            WPI_VictorSPX(id).run {
                follow(rightTalon)
            }
        }

        leftTalon.inverted = true
        arrayOf(leftTalon, rightTalon).forEach {
            it.run {
                // setup feedback
                configFactoryDefault()
                configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 5)
                setSensorPhase(true)
                configContinuousCurrentLimit(30)

                // THE DANGER ZONE
                config_kP(0, 0.05)
                config_kI(0, 0.0)
                config_kD(0, 0.05)
//                config_IntegralZone(0, 4)
                enableVoltageCompensation(true)
                configVoltageCompSaturation(12.0)

                // ramp
                configOpenloopRamp(0.25)

                // neutral mode
                setNeutralMode(NeutralMode.Brake)
                expiration = 0.25
            }
        }

    }

    fun set(left: Double, right: Double, mode: DrivetrainMode) {
        when (mode) {
            DrivetrainMode.DISABLED -> setDrivePercentages(0.0, 0.0)
            DrivetrainMode.DEMO -> setDriveVelocities(left * 0.33, right * 0.33)
            DrivetrainMode.OPEN_LOOP -> setDrivePercentages(left, right)
            DrivetrainMode.CLOSED_LOOP -> setDriveVelocities(left, right)
        }
    }

    private fun setDrivePercentages(left: Double, right: Double) {
        leftTalon.set(left.coerceIn(-1.0, 1.0))
        rightTalon.set(right.coerceIn(-1.0, 1.0))
    }

    fun getVelocities(): Pair<LinearVelocity, LinearVelocity> {
        return leftTalon.selectedSensorVelocity.encoder1024PerDecisecond.toLinearVelocity(wheelRatio) to
                rightTalon.selectedSensorVelocity.encoder1024PerDecisecond.toLinearVelocity(wheelRatio)
    }

    fun getVoltages(): Pair<Double, Double> {
        return leftTalon.motorOutputVoltage to rightTalon.motorOutputVoltage
    }

    /**
     * Sets each side of the drivetrain's velocities as percentages of the maximum
     * @param l Left percent velocity
     * @param r Right percent velocity
     */
    private fun setDriveVelocities(l: Double, r: Double) {

        SmartDashboard.putNumber("Left Error", leftTalon.closedLoopError.toDouble())
        SmartDashboard.putNumber("Right Error", rightTalon.closedLoopError.toDouble())

        val left = (l * maxSpeed.feetPerSecond).feetPerSecond
        val right = (r * maxSpeed.feetPerSecond).feetPerSecond

        val leftNative = left.toAngularVelocity(wheelRatio).encoder1024PerDecisecond
        val rightNative = right.toAngularVelocity(wheelRatio).encoder1024PerDecisecond

        leftTalon.set(ControlMode.Velocity, leftNative, DemandType.ArbitraryFeedForward,
                (kV * left.feetPerSecond + vI * left.feetPerSecond.sign) / 12.0)
        rightTalon.set(ControlMode.Velocity, rightNative, DemandType.ArbitraryFeedForward,
                (kV * right.feetPerSecond + vI * right.feetPerSecond.sign) / 12.0)
    }

    override fun initDefaultCommand() {
        defaultCommand = DefaultDrive()
    }

}