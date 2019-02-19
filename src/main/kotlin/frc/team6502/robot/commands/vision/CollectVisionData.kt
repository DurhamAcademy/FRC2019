package frc.team6502.robot.commands.vision

import edu.wpi.first.wpilibj.command.TimedCommand
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.RobotMap

class CollectVisionData(val timeout: Double, val samples: Int) : TimedCommand(timeout) {

    var tries = 0
    companion object {
        val data = arrayListOf<Triple<Length, Length, Angle>>()
        var avgX = 0.0
        var avgY = 0.0
        var avgH = 0.0
    }

    override fun initialize() {
        println("Starting data collection")
        avgX = 0.0
        avgY = 0.0
        avgH = 0.0
    }

    override fun execute() {
        tries++
        if (RobotMap.kJevois.data.getOrDefault("hasContour", false) as Boolean) {
            val x = RobotMap.kJevois.data.getOrDefault("x", 0.0) as Double
            val y = RobotMap.kJevois.data.getOrDefault("z", 0.0) as Double
            val h = RobotMap.kJevois.data.getOrDefault("yaw", 0.0) as Double
            data.add(Triple(x.inches, y.inches, h.degrees))
        }
    }

    override fun end() {
        // take the average of the data
        avgX = data.map { it.first.feet }.average()
        avgY = data.map { it.second.feet }.average()
        avgH = data.map { it.third.degrees }.average()

        SmartDashboard.putNumber("avgX", avgX)
        SmartDashboard.putNumber("avgY", avgY)
        SmartDashboard.putNumber("avgH", avgH)
    }

    override fun isFinished() = data.size >= samples || tries > 100
}