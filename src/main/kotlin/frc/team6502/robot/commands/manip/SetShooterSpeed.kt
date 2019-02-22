package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.subsystems.CargoIntake

class SetShooterSpeed(val speed: Double) : InstantCommand() {
    //private val shooterActiveTimer = Timer()

    override fun execute() {

        CargoIntake.speedShooter = speed
//        shooterActiveTimer.reset()
//        shooterActiveTimer.start()
    }
}