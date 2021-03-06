
package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.FeedbackDevice
import com.ctre.phoenix.motorcontrol.NeutralMode
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.*

/**
 * Robot's elevator
 */
object Elevator : Subsystem() {

    val elevatorTalon = WPI_TalonSRX(RobotMap.elevatorTalonId)

    private val wheelRatio = (Math.PI * 1.0).inches.meters / 1.rotations.radians
    private var setpoint = 0.0

    init {
        // configure the elevator talon
        elevatorTalon.run {
            // make watchdog less strict
            expiration = 0.05

            // reset to make sure no settings stick
            configFactoryDefault()

            // set up encoder
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 5)
            setSensorPhase(true)

            configForwardSoftLimitEnable(true)
            configForwardSoftLimitThreshold(74700)

            // temporarily zero the sensor until properly zeroed
//            selectedSensorPosition = 0

            // set PID
            config_kP(0, ELEVATOR_GAINS.p)
            config_kI(0, ELEVATOR_GAINS.i)
            config_IntegralZone(0, 16)
            config_kD(0, ELEVATOR_GAINS.d)

            config_kF(0, 0.167726816)

//            configPeak

            // limits and ramps
            configContinuousCurrentLimit(3)
            configPeakCurrentLimit(4)
            configPeakCurrentDuration(500)
            enableCurrentLimit(true)

            configOpenloopRamp(0.5)
            configClosedloopRamp(0.5)

            // brakes on
            setNeutralMode(NeutralMode.Brake)

            // config vel and accel
            configMotionCruiseVelocity(CRUISE_UP.toAngularVelocity(wheelRatio).encoder1024PerDecisecond.toInt())
            configMotionAcceleration(ACCEL_UP.toAngularVelocity(wheelRatio).encoder1024PerDecisecond.toInt())

            // limit switch
//            configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen)
//            configClearPositionOnLimitR(true, 5)
        }

        // set up elevator slaves
        for (id in RobotMap.elevatorVictorIds) {
            WPI_VictorSPX(id).run {
                follow(elevatorTalon)
                setNeutralMode(NeutralMode.Brake)
                expiration = 0.25
            }
        }

        zeroHeight()
    }

    fun zeroHeight() {
//        zeroing = true
        // start the zero elevator cmd
//        elevatorTalon.selectedSensorPosition = 0
        println("ZEROED ELEVATOR (NOT)")
    }

    fun testElevatorOk() {
        elevatorTalon.outputCurrent
        if (elevatorTalon.activeTrajectoryVelocity - elevatorTalon.selectedSensorVelocity > 10) {
            println("Stall!!")
        }
        if(elevatorTalon.errorDerivative > 2000) {
            println("Rope breakage!!")
        }
    }
    /**
     * Height of the elevator in feet
     */
    var height
        get() = (elevatorTalon.getSelectedSensorPosition(0).encoder1024.radians * wheelRatio).meters.feet
        set(value) {
            setpoint = value
            updateSetpoint()
        }

    var sensorPosition = 0
        get() = elevatorTalon.selectedSensorPosition
        private set

    /**
     * Elevator offset from base level
     */
    var offset: Double = 0.0

    /**
     * Percent voltage motors driven by
     */
    var percentVoltage
        get() = elevatorTalon.motorOutputPercent
        set(value) {
            elevatorTalon.set(ControlMode.PercentOutput, value)
        }

    fun updateSetpoint() {
        var offsetAmount = offset

        // prevent elevator from overrunning
        if (OI.selectedElevatorHeight == 2 && RobotStatus.cargoStatus != CargoStatus.NONE) {
            offsetAmount = 0.0
        }

        // calculate desired encoder position for height
        val desired = ((setpoint + offsetAmount).coerceAtLeast(0.0).feet.meters / wheelRatio).radians.encoder1024

        // if elevator wants to go up, use upwards vel and accel, else use downwards ones
        if (desired > elevatorTalon.selectedSensorPosition) {
            elevatorTalon.configMotionCruiseVelocity(CRUISE_UP.toAngularVelocity(wheelRatio).encoder1024PerDecisecond.toInt())
            elevatorTalon.configMotionAcceleration(ACCEL_UP.toAngularVelocity(wheelRatio).encoder1024PerDecisecond.toInt())
        } else {
            elevatorTalon.configMotionCruiseVelocity(CRUISE_DOWN.toAngularVelocity(wheelRatio).encoder1024PerDecisecond.toInt())
            elevatorTalon.configMotionAcceleration(ACCEL_DOWN.toAngularVelocity(wheelRatio).encoder1024PerDecisecond.toInt())
        }

        elevatorTalon.set(ControlMode.MotionMagic, desired)
    }
    override fun initDefaultCommand() {
        defaultCommand = null
    }

}