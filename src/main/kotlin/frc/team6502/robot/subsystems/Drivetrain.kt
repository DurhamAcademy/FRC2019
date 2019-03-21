package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.kyberlib.util.units.LinearVelocity
import frc.team6502.kyberlib.util.units.encoder1024PerDecisecond
import frc.team6502.kyberlib.util.units.feetPerSecond
import frc.team6502.robot.*
import frc.team6502.robot.commands.drive.DefaultDrive
import kotlin.math.sign

/**
 * The robot's drivetrain
 */
object Drivetrain :  Subsystem() {

    private val leftTalon = WPI_TalonSRX(RobotMap.leftTalonId)
    private val rightTalon = WPI_TalonSRX(RobotMap.rightTalonId)



    init {
        // follower victors
        for(id in RobotMap.leftVictorIds){
            WPI_VictorSPX(id).run {
                follow(leftTalon)
            }
        }

        for (id in RobotMap.rightVictorIds) {
            WPI_VictorSPX(id).run {
                follow(rightTalon)
                inverted = true
            }
        }
        // properly invert the right side of the drivetrain
        rightTalon.inverted = true

        // config main drive talons
        arrayOf(leftTalon, rightTalon).forEach {
            it.run {
                // setup feedback
                configFactoryDefault()
                configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 5)
                setSensorPhase(true)
                configContinuousCurrentLimit(30)

                // THE DANGER ZONE
                config_kP(0, 0.1)
                config_kI(0, 0.0)
                config_kD(0, 0.05)
//                config_IntegralZone(0, 4)

                // turn on voltage comp, talons drive faster when voltage dips to compensate
                enableVoltageCompensation(true)
                configVoltageCompSaturation(12.0)

                // ramping
                configClosedloopRamp(0.5)
                configContinuousCurrentLimit(30)

                // neutral mode
                setNeutralMode(NeutralMode.Brake)

                // make watchdog not cry
                expiration = 0.25
            }
        }

    }

    /**
     * Sets the right and left velocities using the given mode
     */
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

    /**
     * Gets each side of the drivetrain's speed
     */
    fun getVelocities(): Pair<LinearVelocity, LinearVelocity> {
        return leftTalon.selectedSensorVelocity.encoder1024PerDecisecond.toLinearVelocity(DRIVETRAIN_WHEEL_RATIO) to
                rightTalon.selectedSensorVelocity.encoder1024PerDecisecond.toLinearVelocity(DRIVETRAIN_WHEEL_RATIO)
    }

    /**
     * Gets voltages used by each side of drivetrain
     */
    fun getVoltages(): Pair<Double, Double> {
        return leftTalon.motorOutputVoltage to rightTalon.motorOutputVoltage
    }

    /**
     * Sets each side of the drivetrain's velocities as percentages of the maximum
     * @param l Left percent velocity
     * @param r Right percent velocity
     */
    private fun setDriveVelocities(l: Double, r: Double) {

        // logging
//        SmartDashboard.putNumber("Left Error", leftTalon.closedLoopError.toDouble())
//        SmartDashboard.putNumber("Right Error", rightTalon.closedLoopError.toDouble())

        // convert % to fps
        val left = (l * DRIVETRAIN_MAXSPEED.feetPerSecond).feetPerSecond
        val right = (r * DRIVETRAIN_MAXSPEED.feetPerSecond).feetPerSecond

        // convert fps to encoder units
        val leftNative = left.toAngularVelocity(DRIVETRAIN_WHEEL_RATIO).encoder1024PerDecisecond
        val rightNative = right.toAngularVelocity(DRIVETRAIN_WHEEL_RATIO).encoder1024PerDecisecond

        val lFF = if (l != 0.0) (kV_L * left.feetPerSecond + kS_L * left.feetPerSecond.sign) / 12.0 else 0.0
        val rFF = if (r != 0.0) (kV_R * right.feetPerSecond + kS_R * right.feetPerSecond.sign) / 12.0 else 0.0
        // set speeds
        leftTalon.set(ControlMode.Velocity, leftNative, DemandType.ArbitraryFeedForward, lFF)
        rightTalon.set(ControlMode.Velocity, rightNative, DemandType.ArbitraryFeedForward, rFF)

        // logging
//        SmartDashboard.putNumber("LEFT DESIRED", left.feetPerSecond)
//        SmartDashboard.putNumber("RIGHT DESIRED", right.feetPerSecond)
    }

    override fun initDefaultCommand() {
        defaultCommand = DefaultDrive()
    }

}