package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.OI
import frc.team6502.robot.subsystems.CargoIntake

class ManipulateCargo : InstantCommand() {

    companion object {
        var hasCargo = false

    }

    init {
        requires(CargoIntake)
        SmartDashboard.putBoolean("Has Cargo", hasCargo)
    }

    override fun execute() {
        hasCargo = SmartDashboard.getBoolean("Has Cargo", false)
        if (hasCargo) {
            ShootCargo(OI.selectedElevatorHeight == 2).start()
        } else {
            IntakeCargo().start()
        }
        hasCargo = !hasCargo
        SmartDashboard.putBoolean("Has Cargo", hasCargo)
    }
}