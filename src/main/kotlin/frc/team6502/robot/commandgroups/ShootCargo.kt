package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team6502.robot.CargoStatus
import frc.team6502.robot.RobotStatus
import frc.team6502.robot.commands.manip.LambdaCommand
import frc.team6502.robot.commands.manip.SetShooterSpeed

class ShootCargo : CommandGroup() {
    init {
        addSequential(SetShooterSpeed(1.0))
        addSequential(WaitCommand(1.0))
        addSequential(LambdaCommand { RobotStatus.setStatusCargo(CargoStatus.NONE) })
        addSequential(SetShooterSpeed(0.0))
    }
}