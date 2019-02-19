package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.ElevatorOffset
import frc.team6502.robot.subsystems.Elevator

class SetElevatorOffset(val offset: ElevatorOffset) : InstantCommand() {

    init {
//        requires(Elevator)
    }

    override fun execute() {
        Elevator.offset = offset
    }
}