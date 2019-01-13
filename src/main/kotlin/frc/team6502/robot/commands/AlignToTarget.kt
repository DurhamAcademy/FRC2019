package frc.team6502.robot.commands

import edu.wpi.first.wpilibj.command.PIDCommand
import frc.team6502.robot.RobotMap

class AlignToTarget: PIDCommand(0.0, 0.0, 0.0){

    var correction = 0.0

    init {
        requires(RobotMap.kDrivetrain)
    }

    override fun initialize() {

    }

    override fun execute() {

    }

    override fun end() {

    }

    override fun isFinished(): Boolean = false

    override fun usePIDOutput(output: Double) {
       correction = output
    }

    override fun returnPIDInput(): Double {
//        return RobotMap.kJevois.data["xOffset"] as Double? ?: 0.0
        return 0.0
    }

}