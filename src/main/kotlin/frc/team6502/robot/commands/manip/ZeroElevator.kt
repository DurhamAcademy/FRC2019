package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.Command
import frc.team6502.robot.subsystems.Elevator

class ZeroElevator() : Command() {
    init {
        requires(Elevator)
    }

    override fun execute() {
        Elevator.percentVoltage = -0.1
    }

    override fun end() {
        Elevator.percentVoltage = 0.0
        Elevator.elevatorTalon.selectedSensorPosition = 0
        Elevator.zeroing = false
    }

    override fun isFinished(): Boolean {
        return Elevator.elevatorTalon.sensorCollection.isRevLimitSwitchClosed
    }
}