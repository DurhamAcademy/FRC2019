package frc.team6502.robot.commands.vision

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.RobotMap

class SetLEDRing(private val on: Boolean) : InstantCommand() {
    override fun execute() {
        RobotMap.kLEDRingRelay.set(on)
    }
}