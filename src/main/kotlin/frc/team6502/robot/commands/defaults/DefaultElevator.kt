package frc.team6502.robot.commands.defaults

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.command.Command
import frc.team6502.robot.OI
import frc.team6502.robot.subsystems.Elevator

class DefaultElevator() : Command() {

    init {
        requires(Elevator)
    }

    override fun execute() {
        Elevator.percentVoltage = OI.controller.getY(GenericHID.Hand.kLeft)
    }

    override fun isFinished() = false

}