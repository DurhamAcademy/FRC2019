package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.CargoStatus
import frc.team6502.robot.HatchStatus
import frc.team6502.robot.RobotStatus
import frc.team6502.robot.commandgroups.ShootCargo
import frc.team6502.robot.subsystems.CargoIntake

class ManipulateCargo(val cargoShip: Boolean) : InstantCommand() {

    init {
        requires(CargoIntake)
    }

    override fun execute() {
        if (RobotStatus.hatchStatus != HatchStatus.NONE) return
        when (RobotStatus.cargoStatus) {
            CargoStatus.NONE -> IntakeCargo(cargoShip).start()
            CargoStatus.IDLE -> {
                if (cargoShip) RobotStatus.setStatusCargo(CargoStatus.ARMED_SHIP)
                else RobotStatus.setStatusCargo(CargoStatus.ARMED_ROCKET)
            }
            CargoStatus.ARMED_SHIP, CargoStatus.ARMED_ROCKET -> ShootCargo().start()
            else -> {
            }
        }
    }
}