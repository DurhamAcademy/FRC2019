package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.kyberlib.util.units.encoder1024
import frc.team6502.kyberlib.util.units.feet
import frc.team6502.kyberlib.util.units.inches
import frc.team6502.robot.subsystems.Elevator

class ZeroElevator : InstantCommand() {
    init {
        requires(Elevator)
    }

    override fun execute() {
        println("Elevator distance from zero: " + Math.abs(Elevator.elevatorTalon.selectedSensorPosition - Elevator.expectedEncoderZeroedPosition).inches)
        Elevator.elevatorTalon.selectedSensorPosition = 0
    }
}