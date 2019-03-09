package frc.team6502.robot.commandgroups

import edu.wpi.first.wpilibj.command.CommandGroup
import edu.wpi.first.wpilibj.command.WaitCommand
import frc.team6502.kyberlib.util.units.inches
import frc.team6502.robot.ElevatorOffset
import frc.team6502.robot.GamePiece
import frc.team6502.robot.OI
import frc.team6502.robot.commands.manip.*

class ShootCargo(val levelThree: Boolean, val cargoShip: Boolean) : CommandGroup() {
    init {
        if (levelThree) addSequential(SetCargoRamp(true))
        if (levelThree) addSequential(SetElevatorOffset(ElevatorOffset.CARGO_L3_DELIVERY))
        if (cargoShip) {
            addSequential(SetElevatorHeight(28.inches))
            addSequential(SetElevatorOffset(ElevatorOffset.INTAKE))
            addSequential(WaitCommand(1.0))
        }
        addSequential(WaitCommand(1.0))
        addSequential(SetShooterSpeed(if (levelThree) -1.0 else 1.0))
        addSequential(WaitCommand(1.5))
        addSequential(SetGamePiece(GamePiece.NONE))
        addSequential(SetShooterSpeed(0.0))
        if (levelThree) addSequential(SetCargoRamp(false))
        if(cargoShip) {
            addSequential(LambdaCommand {
                OI.setElevatorHeight(OI.selectedElevatorHeight)
            })
        }
    }
}