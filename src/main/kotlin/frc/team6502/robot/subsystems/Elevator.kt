package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.robot.RobotMap

object Elevator : Subsystem() {

    val HEIGHT_ZERO = 0.0
    val HEIGHT_L1 = 1.0
    val HEIGHT_L2 = 2.0
    val HEIGHT_L3 = 3.0

    private val elevatorTalon = WPI_TalonSRX(RobotMap.elevatorTalonId)

    init {
        for (id in RobotMap.elevtatorVictorIds) {
            WPI_VictorSPX(id).run {
                follow(elevatorTalon)
                configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0)
                setSelectedSensorPosition(0, 0, 5)
//                configMotionCruiseVelocity()
            }
        }
    }

    var height = 0.0
    set(value) {
        elevatorTalon.set(ControlMode.MotionMagic, 0.0)
    }

    override fun initDefaultCommand() {
        defaultCommand = null
    }

}