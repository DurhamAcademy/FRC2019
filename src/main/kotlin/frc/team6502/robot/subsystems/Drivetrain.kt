package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.robot.RobotMap

object Drivetrain :  Subsystem() {

    val leftTalon = WPI_TalonSRX(RobotMap.leftTalonId)
    val rightTalon = WPI_TalonSRX(RobotMap.rightTalonId)

    val kV = 0.0
    val vI = 0.0

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
            }
        }

        arrayOf(leftTalon, rightTalon).forEach {
            it.run {
                // setup feedback
                configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 5)


                // configure PID controllers
                config_kP(0, 0.0)
                config_kI(0, 0.0)
                config_kD(0, 0.0)

                // ramp
                configClosedloopRamp(0.5)

                // neutral mode
                setNeutralMode(NeutralMode.Brake)
                expiration = 0.25
            }
        }

    }

    fun setDrivePercentages(left: Double, right: Double){

    }

    fun setDriveVelocities(left: Double, right: Double){
        // native velocities are encoder counts per 100ms
        // 1 count = 4
        val leftNative = ((left / 0.5) / 10) * (1024 * 4)
        val rightNative = ((right / 0.5) / 10) * (1024 * 4)

        leftTalon.set(ControlMode.Velocity, leftNative, DemandType.ArbitraryFeedForward, (kV * left + vI) / 12.0)
        leftTalon.set(ControlMode.Velocity, rightNative, DemandType.ArbitraryFeedForward, (kV * right + vI) / 12.0)
    }

    // neutral mode switching
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
        defaultCommand = null
    }

}