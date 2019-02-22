package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team6502.robot.ElevatorOffset

class ShootCargo(val levelThree: Boolean) : CommandGroup() {
    init {
        if (levelThree) addSequential(SetCargoRamp(true))
        if (levelThree) addSequential(SetElevatorOffset(ElevatorOffset.CARGO_L3_DELIVERY))
        else addSequential(SetElevatorOffset(ElevatorOffset.CARGO_DELIVERY))
        addSequential(WaitCommand(1.5))
        addSequential(SetShooterSpeed(if (levelThree) -1.0 else 1.0))
        addSequential(WaitCommand(1.5))
        addSequential(SetShooterSpeed(0.0))
        if (levelThree) addSequential(SetCargoRamp(false))
        addSequential(SetElevatorOffset(ElevatorOffset.CARRY))
    }
}