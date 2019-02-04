
package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.*
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.RobotMap

object Elevator : Subsystem() {

    val HEIGHT_ZERO = 0.0
    val HEIGHT_L1 = 1.0
    val HEIGHT_L2 = 2.0
    val HEIGHT_L3 = 3.0

    val elevatorTalon = WPI_TalonSRX(RobotMap.elevatorTalonId)

    private val cruiseVelocity = 3.feetPerSecond
    private val maxAcceleration = 3.feetPerSecond
    private val holdVoltage = 0.1

    private val wheelRatio = (Math.PI * 1.05).inches.meters / 1.rotations.radians

    init {
        elevatorTalon.run {
            expiration = 0.25
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
            setSelectedSensorPosition(0, 0, 5)
            config_kP(0, 0.1)
            config_kI(0, 0.0)
            config_kD(0, 0.0)
            configMotionCruiseVelocity(cruiseVelocity.toAngularVelocity(wheelRatio).encoder1024PerDecisecond.toInt())
            configMotionAcceleration(maxAcceleration.toAngularVelocity(wheelRatio).encoder1024PerDecisecond.toInt())
        }

        for (id in RobotMap.elevatorVictorIds) {
            WPI_VictorSPX(id).run {
                follow(elevatorTalon)
                expiration = 0.25
            }
        }
    }

    var height
        get() = (elevatorTalon.getSelectedSensorPosition(0).encoder1024.radians * wheelRatio).meters.feet
        set(value) {
            elevatorTalon.set(ControlMode.MotionMagic, (value.feet.meters / wheelRatio).radians.encoder1024, DemandType.ArbitraryFeedForward, holdVoltage / 12.0)
        }

    override fun initDefaultCommand() {
        defaultCommand = null
    }

}