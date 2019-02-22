package frc.team6502.robot.commands.drive

import edu.wpi.first.wpilibj.command.PIDCommand
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.Modes
import frc.team6502.robot.OI
import frc.team6502.robot.RobotMap
import frc.team6502.robot.commands.vision.SetLEDRing
import frc.team6502.robot.subsystems.Drivetrain
import frc.team6502.robot.zero
import kotlin.math.absoluteValue

/**
 * Normal drive with sticks, but vision helps steer
 */
class VisionDrive : PIDCommand(0.01, 0.0, 0.01) {
    private val correctionLimit = 0.33

    override fun returnPIDInput(): Double {
        return RobotMap.kJevois.data.getOrDefault("x", 0.0) as Double
    }

    override fun usePIDOutput(output: Double) {
        yawCorrection = output.coerceIn(-correctionLimit, correctionLimit)
    }

    // YAW CORRECTION
    private var yawCorrection = 0.0

    // CURVATURE PARAMS
    private var quickStopAccumulator = 0.0
    private val quickStopThreshold = 0.2
    private val quickStopAlpha = 0.1

    init {
        requires(Drivetrain)
    }

    override fun initialize() {
        println("STARTING VISIONDRIVE")
        SetLEDRing(true).start()
        RobotMap.kIMU.zero()

        yawCorrection = 0.0
//        println("reset pigeon")
    }

    override fun execute() {

        val throttle = OI.commandedY * -1

        if (yawCorrection.absoluteValue < 0.05 && throttle == 0.0) {
            yawCorrection = 0.0
        }

        val rotation = OI.commandedX + yawCorrection * OI.commandedY.absoluteValue

        curvatureDrive(throttle, rotation, true)
        SmartDashboard.putNumber("Vision Correction", 0.0)
    }


    override fun isFinished() = OI.controller.aButtonPressed || !(RobotMap.kJevois.data.getOrDefault("hasContour", false) as Boolean)

    override fun end() {
        SetLEDRing(false).start()
    }

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
        Drivetrain.set(leftMotorOutput, rightMotorOutput, Modes.drivetrainMode.selected)
//        println("left out=${leftMotorOutput}")
    }
}