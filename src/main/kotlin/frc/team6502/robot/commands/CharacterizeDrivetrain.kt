package frc.team6502.robot.commands

import edu.wpi.first.wpilibj.command.TimedCommand
import frc.team6502.robot.subsystems.Drivetrain
import org.nield.kotlinstatistics.simpleRegression
import java.io.File

class CharacterizeDrivetrain() : TimedCommand(3.5) {

    val rampSpeed = 1.0 / 500
    private var currentSpeed = 0.0

    private val leftPoints = arrayListOf<Pair<Double, Double>>()
    private val rightPoints = arrayListOf<Pair<Double, Double>>()

    val f = File("/U/characterizationData.txt")

    init {
        requires(Drivetrain)
    }

    override fun initialize() {
        Drivetrain.setDrivePercentages(0.0, 0.0)
        f.writeText("")
    }

    override fun execute() {
        currentSpeed += rampSpeed
        currentSpeed.coerceAtMost(1.0)

        Drivetrain.setDrivePercentages(currentSpeed, currentSpeed)

        val velocities = Drivetrain.getVelocities()
        val voltages = Drivetrain.getVoltages()

        if (velocities.first.feetPerSecond > 0.1 && voltages.first != 0.0 && velocities.second.feetPerSecond > 0.1 && voltages.second != 0.0) {
            leftPoints.add(velocities.first.feetPerSecond to voltages.first)
            rightPoints.add(velocities.second.feetPerSecond to voltages.second)
        }
    }

    override fun end() {
        Drivetrain.setDrivePercentages(0.0, 0.0)
        val lReg = leftPoints.simpleRegression()
        val rReg = rightPoints.simpleRegression()
        f.appendText("LEFT kV=${lReg.slope} vI=${lReg.intercept} r^2=${lReg.sumSquaredErrors}\n")
        f.appendText("RIGHT kV=${rReg.slope} vI=${rReg.intercept} r^2=${rReg.sumSquaredErrors}\n")
    }

}