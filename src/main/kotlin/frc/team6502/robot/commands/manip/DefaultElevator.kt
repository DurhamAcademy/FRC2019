package frc.team6502.robot.commands.manip

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.wpilibj.command.Command
import frc.team6502.robot.subsystems.Elevator

class DefaultElevator() : Command() {
    init {
        requires(Elevator)
    }

    override fun initialize() {
        Elevator.elevatorTalon.set(ControlMode.Position, Elevator.elevatorTalon.selectedSensorPosition.toDouble())
    }

    override fun execute() {
    }

    override fun isFinished() = false

}