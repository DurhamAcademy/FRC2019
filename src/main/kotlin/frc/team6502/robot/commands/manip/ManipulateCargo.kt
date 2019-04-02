package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.*
import frc.team6502.robot.commandgroups.ShootCargo

class ManipulateCargo(val cargoShip: Boolean) : InstantCommand() {

    init {
//        requires(CargoIntake)
    }

    override fun execute() {
        println("MANIPULATING CARGO")
        if (RobotStatus.cargoStatus == CargoStatus.INTAKING_STATION || RobotStatus.cargoStatus == CargoStatus.INTAKING_GROUND) return
        if (RobotStatus.hatchStatus != HatchStatus.NONE) return
        when (RobotStatus.cargoStatus) {
            CargoStatus.NONE -> IntakeCargo(cargoShip).start()
            CargoStatus.IDLE -> {
                if (cargoShip) RobotStatus.setStatusCargo(CargoStatus.ARMED_SHIP)
                else RobotStatus.setStatusCargo(CargoStatus.ARMED_ROCKET)
            }
            CargoStatus.ARMED_SHIP -> {
                if (!cargoShip) RobotStatus.setStatusCargo(CargoStatus.ARMED_ROCKET)
                else ShootCargo().start()
            }
            CargoStatus.ARMED_ROCKET -> {
                if (cargoShip) RobotStatus.setStatusCargo(CargoStatus.ARMED_SHIP)
                else ShootCargo().start()
            }
            else -> {
            }
        }
    }
}