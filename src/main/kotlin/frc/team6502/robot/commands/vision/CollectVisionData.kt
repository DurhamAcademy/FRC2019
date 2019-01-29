package frc.team6502.robot.commands.vision

import edu.wpi.first.wpilibj.command.TimedCommand
import frc.team6502.kyberlib.util.units.*
import frc.team6502.robot.RobotMap
import org.nield.kotlinstatistics.median

class CollectVisionData(val timeout: Double, val samples: Int) : TimedCommand(timeout) {

    companion object {
        val data = arrayListOf<Triple<Length, Length, Angle>>()
        var avgX = 0.0
        var avgY = 0.0
        var avgH = 0.0
    }

    override fun initialize() {
        println("Starting data collection")
    }

    override fun execute() {
        val x = RobotMap.kJevois.data.getOrDefault("x", 0.0) as Double
        val y = RobotMap.kJevois.data.getOrDefault("z", 0.0) as Double
        val h = RobotMap.kJevois.data.getOrDefault("yaw", 0.0) as Double
        data.add(Triple(x.inches, y.inches, h.degrees))
    }

    override fun end() {
        // take the average of the data
        avgX = data.map { it.first.feet }.median()
        avgY = data.map { it.second.feet }.median()
        avgH = data.map { it.third.degrees }.median()
    }

    override fun isFinished() = data.size >= samples
}