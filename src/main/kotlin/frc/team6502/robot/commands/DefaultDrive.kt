package frc.team6502.robot.commands

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.PIDController
import edu.wpi.first.wpilibj.PIDSourceType
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.*
import frc.team6502.robot.subsystems.Drivetrain
import kotlin.math.absoluteValue

class DefaultDrive : Command() {

    // YAW CORRECTION
    private var yawCorrection = 0.0
    private var yawCorrecting = false
    private val yawTimer = Timer()
    private val yawController = PIDController(0.0, 0.0, 0.0, ArbitraryPIDSource(PIDSourceType.kDisplacement) { RobotMap.kIMU.getYaw().halfDegrees }) {
        yawCorrection = it.coerceIn(-0.1, 0.1)
    }

    // CURVATURE PARAMS
    private var quickStopAccumulator = 0.0
    private val quickStopThreshold = 0.2
    private val quickStopAlpha = 0.1

    init {
        requires(Drivetrain)
    }

    override fun initialize() {
        RobotMap.kIMU.zero()
    }

    override fun execute() {
        val throttle = OI.controller.y
        val rotation = OI.controller.x

        if (yawCorrection.absoluteValue < 0.05 && throttle == 0.0) {
            yawCorrection = 0.0
        }

        if (yawCorrecting) {
            Drivetrain.setDriveVelocities(throttle - yawCorrection, throttle + yawCorrection)
            SmartDashboard.putNumber("Yaw Correction", yawCorrection)
        } else {
            curvatureDrive(throttle, rotation, true)
            SmartDashboard.putNumber("Yaw Correction", 0.0)
        }

        if (OI.commandingStraight) {
            yawTimer.reset()
            yawTimer.start()
            yawCorrecting = false
        } else if (yawTimer.get() > 0.25 && !yawCorrecting) {
            yawCorrecting = true
            RobotMap.kIMU.zero()
        }

        // tip warning
        OI.setControllerRumble(OI.deadband((RobotMap.kIMU.getPitch().absoluteValue / 45.0).coerceIn(0.0, 1.0), 0.05))

        // allow for sliding
        Drivetrain.brakeMode = !OI.controller.getBumper(GenericHID.Hand.kLeft)
    }


    override fun isFinished() = false

    /**
     * Curvature drive method for differential drive platform.
     *
     * The rotation argument controls the curvature of the robot's path rather than its rate of heading change. This
     * makes the robot more controllable at high speeds. Also handles the robot's quick turn functionality -
     * "quick turn" overrides constant-curvature turning for turn-in-place maneuvers.
     *
     * @param speed The robot's speed along the X axis [-1.0..1.0]. Forward is positive.
     * @param rotation The robot's rotation rate around the Z axis [-1.0..1.0]. Clockwise is positive.
     * @param isQuickTurn If set, overrides constant-curvature turning for turn-in-place maneuvers.
     */
    private fun curvatureDrive(speed: Double, rotation: Double, isQuickTurn: Boolean) {

        val angularPower: Double
        val overPower: Boolean

        if (isQuickTurn) {
            if (speed.absoluteValue < quickStopThreshold) {
                quickStopAccumulator = (1 - quickStopAlpha) * quickStopAccumulator + quickStopAlpha * rotation * 2.0
            }
            overPower = true
            angularPower = rotation

        } else {
            overPower = false
            angularPower = speed.absoluteValue * rotation - quickStopAccumulator

            when {
                quickStopAccumulator > 1 -> quickStopAccumulator -= 1.0
                quickStopAccumulator < -1 -> quickStopAccumulator += 1.0
                else -> quickStopAccumulator = 0.0
            }
        }

        var leftMotorOutput = speed + angularPower
        var rightMotorOutput = speed - angularPower

        // If rotation is overpowered, reduce both outputs to within acceptable range
        if (overPower) {
            when {
                leftMotorOutput > 1.0 -> {
                    rightMotorOutput -= leftMotorOutput - 1.0
                    leftMotorOutput = 1.0
                }
                rightMotorOutput > 1.0 -> {
                    leftMotorOutput -= rightMotorOutput - 1.0
                    rightMotorOutput = 1.0
                }
                leftMotorOutput < -1.0 -> {
                    rightMotorOutput -= leftMotorOutput + 1.0
                    leftMotorOutput = -1.0
                }
                rightMotorOutput < -1.0 -> {
                    leftMotorOutput -= rightMotorOutput + 1.0
                    rightMotorOutput = -1.0
                }
            }
        }

        Drivetrain.setDriveVelocities(leftMotorOutput, rightMotorOutput)
    }
}