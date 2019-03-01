package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.GamePiece
import frc.team6502.robot.OI
import frc.team6502.robot.RobotStatus
import frc.team6502.robot.commandgroups.ShootCargo
import frc.team6502.robot.subsystems.CargoIntake

class ManipulateCargo : InstantCommand() {

    init {
        requires(CargoIntake)
    }

    override fun execute() {
        when {
            RobotStatus.currentGamePiece == GamePiece.CARGO -> ShootCargo(OI.selectedElevatorHeight == 2).start()
            RobotStatus.currentGamePiece == GamePiece.NONE -> IntakeCargo().start()
        }
    }
}