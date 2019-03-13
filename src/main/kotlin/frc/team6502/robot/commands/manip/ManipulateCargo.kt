package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.GamePiece
import frc.team6502.robot.OI
import frc.team6502.robot.RobotStatus
import frc.team6502.robot.commandgroups.ShootCargo
import frc.team6502.robot.subsystems.CargoIntake

class ManipulateCargo(val cargoShip: Boolean) : InstantCommand() {

    init {
        requires(CargoIntake)
    }

    override fun execute() {
        when {
            RobotStatus.currentGamePiece == GamePiece.CARGO -> ShootCargo(cargoShip).start()
            RobotStatus.currentGamePiece == GamePiece.NONE -> IntakeCargo(OI.selectedElevatorHeight == 1).start()
        }
    }
}