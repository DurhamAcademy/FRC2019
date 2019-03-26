package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.kyberlib.util.units.Length
import frc.team6502.robot.ElevatorOffset
import frc.team6502.robot.subsystems.Elevator

class SetElevatorOffset(val offset: Length) : InstantCommand() {

    init {
//        requires(Elevator)
        setRunWhenDisabled(true)
    }

    override fun execute() {
        Elevator.offset = offset.feet
        Elevator.updateSetpoint()
    }
}