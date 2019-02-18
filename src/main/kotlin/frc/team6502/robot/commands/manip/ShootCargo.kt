package frc.team6502.robot.commands.manip
import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.Command
import frc.team6502.robot.subsystems.CargoIntake

class ShootCargo() : Command(1.5){
    private val shooterActiveTimer = Timer()
    init {
        requires(CargoIntake)
    }

    override fun initialize() {
        CargoIntake.speedShooter = 1.0
        shooterActiveTimer.reset()
        shooterActiveTimer.start()
    }
    override fun end() {
        CargoIntake.speedShooter = 0.0
    }

    override fun interrupted() {
        CargoIntake.speedShooter = 0.0
    }
    override fun isFinished(): Boolean {
        return shooterActiveTimer.get() > 3 //Just to test if I need a timer
    }
}