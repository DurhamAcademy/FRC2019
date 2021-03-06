package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.subsystems.Elevator

class ZeroElevator() : InstantCommand() {
    init {
        requires(Elevator)
    }

    override fun execute() {
        Elevator.zeroHeight()
    }
}