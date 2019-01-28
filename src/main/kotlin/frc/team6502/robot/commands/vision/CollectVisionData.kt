package frc.team6502.robot.commands.vision

import edu.wpi.first.wpilibj.command.Command
import frc.team6502.kyberlib.util.units.*

class CollectVisionData : Command() {

    companion object {
        val data = arrayListOf<VisionPosition>()
    }

    override fun initialize() {

    }

    override fun execute() {
        data.add(VisionPosition(0.inches, 0.inches, 0.degrees))
    }

    override fun end() {

    }

    override fun isFinished() = data.size > 10

}

data class VisionPosition(val x: Length, val y: Length, val h: Angle)