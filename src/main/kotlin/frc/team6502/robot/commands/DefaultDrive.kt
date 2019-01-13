package frc.team6502.robot.commands

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.PIDCommand
import frc.team6502.robot.*
import java.lang.Math.abs

class DefaultDrive : PIDCommand(RobotMap.driveStraightPID.p, RobotMap.driveStraightPID.i, RobotMap.driveStraightPID.d) {

    private var correction = 0.0
    private var drivingStraight = false

    private var quickStopAccumulator = 0.0
    private val quickStopThreshold = 0.2
    private val quickStopAlpha = 0.1

    private val holdTimer = Timer()

    init {
        requires(RobotMap.kDrivetrain)
    }

    override fun initialize() {

    }

    override fun execute() {

        val throttle = OI.controller.y
        val rotation = OI.controller.x

        if(abs(correction) < 0.1 && throttle == 0.0){
            correction = 0.0
        }

        if(drivingStraight){
             RobotMap.kDrivetrain.setDriveVelocities(throttle - correction, throttle + correction)
        } else {
            curvatureDrive(throttle, rotation, true)
        }

        if (OI.commandingStraight) {
            holdTimer.reset()
            holdTimer.start()
            drivingStraight = false
        } else if (holdTimer.get() > 0.25 && !drivingStraight) {
            drivingStraight = true
            RobotMap.kIMU.zero()
            println("reset pigeon")
        }

    }

    override fun isFinished(): Boolean {
        return false
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
    fun curvatureDrive(speed: Double, rotation: Double, isQuickTurn: Boolean) {

        val angularPower: Double
        val overPower: Boolean

        if (isQuickTurn) {
            if (abs(speed) < quickStopThreshold) {
                quickStopAccumulator = (1 - quickStopAlpha) * quickStopAccumulator + quickStopAlpha * rotation * 2.0
            }
            overPower = true
            angularPower = rotation

        } else {
            overPower = false
            angularPower = abs(speed) * rotation - quickStopAccumulator

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

        RobotMap.kDrivetrain.setDriveVelocities(leftMotorOutput, rightMotorOutput)
    }

    override fun usePIDOutput(output: Double) {
        correction = output
    }

    override fun returnPIDInput(): Double {
        return RobotMap.kIMU.getYaw()
    }

}