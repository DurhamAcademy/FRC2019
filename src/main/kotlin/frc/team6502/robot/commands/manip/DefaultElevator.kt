package frc.team6502.robot.commands.manip

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.DemandType
import edu.wpi.first.wpilibj.command.Command
import frc.team6502.kyberlib.util.units.feet
import frc.team6502.kyberlib.util.units.radians
import frc.team6502.robot.ElevatorOffset
import frc.team6502.robot.subsystems.Elevator

class DefaultElevator() : Command() {
    init {
        requires(Elevator)
    }

    override fun initialize() {
        Elevator.elevatorTalon.stopMotor()
    }

    override fun execute() {
        val offsetAmount = when (Elevator.offset) {
            ElevatorOffset.CARRY -> 0.0
            ElevatorOffset.CARGO_DELIVERY -> -Elevator.CARGO_DELIVERY_OFFSET
            ElevatorOffset.HATCH_DELIVERY -> Elevator.HATCH_DELIVERY_OFFSET
        }

        // calculate desired encoder position for height
        val desired = ((Elevator.setpoint - offsetAmount - Elevator.GROUND_DISTANCE).coerceAtLeast(0.0).feet.meters / Elevator.wheelRatio).radians.encoder1024

        Elevator.elevatorTalon.set(ControlMode.MotionMagic, desired, DemandType.ArbitraryFeedForward, Elevator.holdVoltage / 12.0)
    }

    override fun isFinished() = false

}