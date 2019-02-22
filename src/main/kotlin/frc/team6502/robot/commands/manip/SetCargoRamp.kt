package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.subsystems.CargoIntake

class SetCargoRamp(val ramp: Boolean) : InstantCommand() {
    override fun execute() {
        CargoIntake.rampState = ramp;
    }
}