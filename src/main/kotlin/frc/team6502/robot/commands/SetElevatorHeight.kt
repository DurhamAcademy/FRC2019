package frc.team6502.robot.commands

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.InstantCommand
import frc.team6502.robot.RobotMap

class SetElevatorHeight(height: Double): InstantCommand() {

    init {
//        requires(RobotMap.kElevator)
    }
    override fun execute() {
//        RobotMap.kElevator.height = height
    }
}