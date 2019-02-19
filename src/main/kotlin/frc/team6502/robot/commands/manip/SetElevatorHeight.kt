package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.kyberlib.util.units.Length
import frc.team6502.robot.subsystems.Elevator

class SetElevatorHeight(private val height: Length) : InstantCommand() {

    init {
//        requires(Elevator)
    }
    override fun execute() {
        Elevator.height = height.feet
    }
}