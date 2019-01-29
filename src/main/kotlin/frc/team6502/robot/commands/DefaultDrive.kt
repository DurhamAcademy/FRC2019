package frc.team6502.robot.commands

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.PIDCommand
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.*
import frc.team6502.robot.subsystems.Drivetrain
import kotlin.math.absoluteValue

// decent gains P=0.02 I=0.0 D=0.02
class DefaultDrive : PIDCommand(0.01, 0.0, 0.03) {
    private val correctionLimit = 0.33
    private var correctionZero = 0.0

    override fun returnPIDInput(): Double {
        return RobotMap.kIMU.getYaw() - correctionZero
    }

    override fun usePIDOutput(output: Double) {
        yawCorrection = output.coerceIn(-correctionLimit, correctionLimit)
    }

    // YAW CORRECTION
    private var yawCorrection = 0.0
    private var yawCorrecting = false
    private val yawTimer = Timer()

    // CURVATURE PARAMS
    private var quickStopAccumulator = 0.0
    private val quickStopThreshold = 0.2
    private val quickStopAlpha = 0.1

    // FRONT TOGGLE
    private var frontIsFront = true

    init {
        requires(Drivetrain)
    }

    override fun initialize() {
        yawTimer.start()
        correctionZero = RobotMap.kIMU.getYaw()
        yawCorrection = 0.0
        yawCorrecting = true
        println("reset pigeon")
    }

    override fun execute() {
        val throttle = OI.commandedY * if (frontIsFront) -1 else 1
        val rotation = OI.commandedX

        if (yawCorrection.absoluteValue < 0.05 && throttle == 0.0) {
            yawCorrection = 0.0
        }

        if (yawTimer.get() < 0.35) {
            yawCorrection = 0.0
            correctionZero = RobotMap.kIMU.getYaw()
        }

        SmartDashboard.putBoolean("Correcting", yawCorrecting)
        if (yawCorrecting) {
            Drivetrain.setDriveVelocities(throttle - yawCorrection, throttle + yawCorrection)
            SmartDashboard.putNumber("Heading Correction", yawCorrection)
        } else {
//            println(rotation)
            curvatureDrive(throttle, rotation, true)
            SmartDashboard.putNumber("Heading Correction", 0.0)
        }

        if (!OI.commandingStraight) {
            yawTimer.reset()
            yawTimer.start()
            yawCorrecting = false
        } else if (yawTimer.get() > 0.3 && !yawCorrecting) {
            correctionZero = RobotMap.kIMU.getYaw()
            yawCorrection = 0.0
            yawCorrecting = true
        }

        SmartDashboard.putNumber("pitch", RobotMap.kIMU.getPitch())

        if (OI.controller.xButtonPressed) {
            frontIsFront = true
        }
        if (OI.controller.yButtonPressed) {
            frontIsFront = false
        }
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

//        println("COMMANDED LEFT: $leftMotorOutput COMMANDED RIGHT: $rightMotorOutput")
        Drivetrain.setDriveVelocities(leftMotorOutput, rightMotorOutput)
//        println("left out=${leftMotorOutput}")
    }
}