package frc.team6502.robot.commands.drive

import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.PIDController
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.PIDCommand
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.*
import frc.team6502.robot.sensor.VisionPIDSource
import frc.team6502.robot.subsystems.Drivetrain
import kotlin.math.absoluteValue

// decent gains P=0.02 I=0.0 D=0.02
class DefaultDrive : PIDCommand(0.01, 0.0, 0.01) {
    private val correctionLimit = 0.33

    override fun returnPIDInput(): Double {
        return RobotMap.kIMU.getYaw()
    }

    override fun usePIDOutput(output: Double) {
//        yawCorrection = output
        yawCorrection = 0.0//OI.deadband(yawCorrection, 0.015)
    }

    // YAW CORRECTION
    private var yawCorrection = 0.0
    private var yawCorrecting = false
    private val yawTimer = Timer()

    private var visionCorrection = 0.0

    private var quickStopAccumulator = 0.0

    // FRONT TOGGLE
    private var frontIsFront = true

    private var visionPIDController = PIDController(0.01, 0.0, 0.01, VisionPIDSource()) {
        visionCorrection = if (OI.commandedVC) it else 0.0
    }

    init {
        requires(Drivetrain)
    }

    override fun initialize() {
        println("STARTING DRIVETRAIN")
        RobotMap.kIMU.zero()
        yawTimer.start()
        visionPIDController.enable()
        yawCorrection = 0.0
        yawCorrecting = true
//        println("reset pigeon")
    }

    override fun execute() {
        val throttle = OI.commandedY * if (frontIsFront) -1 else 1
        val rotation = OI.commandedX

//        println("EXECUTING DRIVETRAIN LOL")

        if (yawCorrection.absoluteValue < 0.05 && throttle == 0.0) {
            yawCorrection = 0.0
        }
//        yawCorrection = 0.0
//        visionCorrection = 0.0
//        println(throttle)
//        if (yawCorrection.absoluteValue < 0.05 && throttle < 0.15 && throttle > 0.0){
//            yawCorrection = 0.0
//        }

        if (yawTimer.get() < 0.35) {
            yawCorrection = 0.0
            RobotMap.kIMU.zero()
        }

        // redundancy department of redundancy
        if (!OI.commandedVC) visionCorrection = 0.0

        SmartDashboard.putBoolean("Correcting", yawCorrecting)
        SmartDashboard.putNumber("Vision Correction", visionCorrection)

//        println("t=$throttle r=$rotation")
        if (yawCorrecting) {
            Drivetrain.set(throttle - yawCorrection, throttle + yawCorrection, DrivetrainMode.CLOSED_LOOP)
            SmartDashboard.putNumber("Heading Correction", yawCorrection)
        } else {
            curvatureDrive(throttle, rotation - visionCorrection, true)
            SmartDashboard.putNumber("Heading Correction", 0.0)
        }

        if (!OI.commandingStraight) {
            yawTimer.reset()
            yawTimer.start()
            yawCorrecting = false
        } else if (yawTimer.get() > 0.3 && !yawCorrecting) {
            RobotMap.kIMU.zero()
            yawCorrection = 0.0
            yawCorrecting = true
        }

        SmartDashboard.putNumber("pitch", RobotMap.kIMU.getPitch())

        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(if(OI.commandedVC) 0 else 1)
        /*if (OI.controller.xButtonPressed) {
            frontIsFront = true
        }
        if (OI.controller.yButtonPressed) {
            frontIsFront = false
        }*/
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
        Drivetrain.set(leftMotorOutput, rightMotorOutput, Modes.drivetrainMode.selected)
//        println("left out=${leftMotorOutput}")
    }
}