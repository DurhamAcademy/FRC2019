
package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.*
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.ElevatorOffset
import frc.team6502.robot.RobotMap
import frc.team6502.robot.commands.manip.ZeroElevator
import kotlin.math.absoluteValue

/**
 * Robot's elevator
 */
object Elevator : Subsystem() {

    val CARGO_DELIVERY_OFFSET = 6.inches.feet
    val HATCH_DELIVERY_OFFSET = 2.4.inches.feet
    val GROUND_DISTANCE = 6.inches.feet

    val elevatorTalon = WPI_TalonSRX(RobotMap.elevatorTalonId)

    private val cruiseVelocity = 2.feetPerSecond
    private val maxAcceleration = 1.feetPerSecond
    private val holdVoltage = 1.1

    private val wheelRatio = (Math.PI * 1.0).inches.meters / 1.rotations.radians
    private var setpoint = 0.0
    var zeroing = false

    init {
        // configure the elevator talon
        elevatorTalon.run {
            // make watchdog less strict
            expiration = 0.25

            // reset to make sure no settings stick
            configFactoryDefault()

            // set up encoder
            configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 5)
            setSensorPhase(true)

            // temporarily zero the sensor until properly zeroed
            selectedSensorPosition = 0

            // set PID
            config_kP(0, 0.3)
            config_kI(0, 0.0)
            config_IntegralZone(0, 64)
            config_kD(0, 0.002)

            // limits and ramps
            configContinuousCurrentLimit(2)
            configOpenloopRamp(0.5)

            // brakes on
            setNeutralMode(NeutralMode.Brake)

            // config vel and accel
            configMotionCruiseVelocity(cruiseVelocity.toAngularVelocity(wheelRatio).encoder1024PerDecisecond.toInt())
            configMotionAcceleration(maxAcceleration.toAngularVelocity(wheelRatio).encoder1024PerDecisecond.toInt())

            // limit switch
            configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen)
            configClearPositionOnLimitR(true, 5)
        }

        // set up elevator slaves
        for (id in RobotMap.elevatorVictorIds) {
            WPI_VictorSPX(id).run {
                follow(elevatorTalon)
                setNeutralMode(NeutralMode.Brake)
                expiration = 0.25
            }
        }

//        zeroHeight()
    }

    override fun periodic() {
        // determine offset based on offset mode
        val offsetAmount = when (offset) {
            ElevatorOffset.CARRY -> 0.0
            ElevatorOffset.CARGO_DELIVERY -> CARGO_DELIVERY_OFFSET
            ElevatorOffset.HATCH_DELIVERY -> HATCH_DELIVERY_OFFSET
        }

        // calculate desired encoder position for height
        val desired = ((setpoint - offsetAmount - GROUND_DISTANCE).coerceAtLeast(0.0).feet.meters / wheelRatio).radians.encoder1024


        // if elevator is stopped slightly above zero then slowly bring it down until limit is hit
        if (!zeroing) {
            if (elevatorTalon.selectedSensorVelocity.absoluteValue < 16 &&
                    setpoint == 0.0 &&
                    elevatorTalon.selectedSensorPosition < 1000
                    && !elevatorTalon.sensorCollection.isRevLimitSwitchClosed) {
                zeroHeight()
            } else {
                // update the motion magic setpoint
                elevatorTalon.set(ControlMode.MotionMagic, desired, DemandType.ArbitraryFeedForward, holdVoltage / 12.0)
            }
        }
    }

    fun zeroHeight() {
        zeroing = true
        // start the zero elevator cmd
        ZeroElevator().start()
    }

    /**
     * Height of the elevator in feet
     */
    var height
        get() = (elevatorTalon.getSelectedSensorPosition(0).encoder1024.radians * wheelRatio).meters.feet
        set(value) {
            setpoint = value
        }

    /**
     * Elevator offset from base level
     */
    var offset: ElevatorOffset = ElevatorOffset.CARRY

    /**
     * Percent voltage motors driven by
     */
    var percentVoltage
        get() = elevatorTalon.motorOutputPercent
        set(value) {
            elevatorTalon.set(ControlMode.PercentOutput, value)
        }

    override fun initDefaultCommand() {
        defaultCommand = null
    }

}