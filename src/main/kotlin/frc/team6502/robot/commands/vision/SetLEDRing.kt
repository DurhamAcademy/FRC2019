package frc.team6502.robot.commands.vision

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.RobotMap

class SetLEDRing(private val on: Boolean) : InstantCommand() {
    companion object {
        var state = false
    }
    override fun execute() {
        state = on
        RobotMap.kJevois.runCommand("setcam absexp " + if(on) "70" else "750")
        RobotMap.kLEDRingRelay.set(on)
    }
}