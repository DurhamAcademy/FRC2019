package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.InstantCommand
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.kyberlib.util.units.inches
import frc.team6502.robot.OI
import frc.team6502.robot.subsystems.CargoIntake
import frc.team6502.robot.subsystems.Elevator

class ManipulateCargo : InstantCommand() {
    init {
        requires(CargoIntake)
    }

    override fun execute() {
        if(Elevator.height < (0.1/12)+6) {
            IntakeCargo()
        }
        else {
            ShootCargo()
        }
    }
}