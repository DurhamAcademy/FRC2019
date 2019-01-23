package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.*
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.command.Subsystem
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.RobotMap
import frc.team6502.robot.commands.DefaultDrive

object Drivetrain :  Subsystem() {

    private val leftTalon = WPI_TalonSRX(RobotMap.leftTalonId)
    private val rightTalon = WPI_TalonSRX(RobotMap.rightTalonId)

    private val maxSpeed = 13.30.feetPerSecond
    private val wheelRatio = (Math.PI * 6.0).inches.meters / 1.rotations.radians

    private val kV = 12.0 / maxSpeed.feetPerSecond
    private val vI = 0.0

    init {
        // probably going to be a wcd
        // two ktalon objects needed, config tbd
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

        rightTalon.inverted = true
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
                config_kD(0, 0.1)
//                config_IntegralZone(0, 4)

                // ramp
                configOpenloopRamp(0.25)

                // neutral mode
                setNeutralMode(NeutralMode.Brake)
                expiration = 0.25
            }
        }

    }

    fun setDrivePercentages(left: Double, right: Double){
        leftTalon.set(left)
        rightTalon.set(right)
    }

    fun getVelocity(): LinearVelocity {
        //TODO:
        return 0.feetPerSecond
    }

    /**
     * Sets each side of the drivetrain's velocities as percentages of the maximum
     * @param l Left percent velocity
     * @param r Right percent velocity
     */
    fun setDriveVelocities(l: Double, r: Double) {

        SmartDashboard.putNumber("Left Error", leftTalon.closedLoopError.toDouble())
        SmartDashboard.putNumber("Right Error", rightTalon.closedLoopError.toDouble())

        val left = (l * maxSpeed.feetPerSecond).feetPerSecond
        val right = (r * maxSpeed.feetPerSecond).feetPerSecond

//        println("left=${left.feetPerSecond} right=${right.feetPerSecond}")

        val leftNative = left.toAngularVelocity(wheelRatio).encoder1024PerDecisecond
        val rightNative = right.toAngularVelocity(wheelRatio).encoder1024PerDecisecond

//        SmartDashboard.putNumber("left tgt", leftTalon.closedLoopTarget.toDouble())
//        SmartDashboard.putNumber("right tgt", rightTalon.closedLoopTarget.toDouble())

//        println("left=${leftNative} right=${rightNative}")

//        leftTalon.set(ControlMode.Velocity, l)
//        rightTalon.set(ControlMode.Velocity, rightNative)
        leftTalon.set(ControlMode.Velocity, leftNative, DemandType.ArbitraryFeedForward, (kV * left.feetPerSecond + vI) / 12.0)
        rightTalon.set(ControlMode.Velocity, rightNative, DemandType.ArbitraryFeedForward, (kV * right.feetPerSecond + vI) / 12.0)
    }

    // deja vu i've just been in this place before higher on the streets
    private var m_brakeMode = true
    var brakeMode
        get() = m_brakeMode
        set(value) {
            if (value != m_brakeMode) {
                arrayOf(leftTalon, rightTalon).forEach {
                    it.setNeutralMode(when (value) {
                        true -> NeutralMode.Brake
                        false -> NeutralMode.Coast
                    })
                }
                m_brakeMode = value
            }
        }

    override fun initDefaultCommand() {
        defaultCommand = DefaultDrive()
    }

}