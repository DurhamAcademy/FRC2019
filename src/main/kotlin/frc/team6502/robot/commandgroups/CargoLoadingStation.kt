package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team6502.kyberlib.util.units.inches
import frc.team6502.robot.ElevatorOffset
import frc.team6502.robot.commands.manip.SetElevatorHeight
import frc.team6502.robot.commands.manip.SetElevatorOffset
import frc.team6502.robot.commands.manip.SetShooterSpeed
import frc.team6502.robot.subsystems.CargoIntake

class CargoLoadingStation() : CommandGroup() {
    init {
        requires(CargoIntake)
        addSequential(SetElevatorHeight(28.inches))
        addSequential(SetElevatorOffset(ElevatorOffset.INTAKE))
        addSequential(SetShooterSpeed(-0.4))
    }
    override fun isFinished(): Boolean {
        return (CargoIntake.shooterCurrent > 1.6)
    }
}