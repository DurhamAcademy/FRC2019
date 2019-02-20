package frc.team6502.robot.commands.vision

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.RobotMap

class SetLEDRing(private val on: Boolean) : InstantCommand() {
    companion object {
        var state = false
    }
    override fun execute() {
        state = !state
//        RobotMap.kJevois.setCam("absexp", if(state) "150" else "500")
        RobotMap.kLEDRingRelay.set(on)
    }
}