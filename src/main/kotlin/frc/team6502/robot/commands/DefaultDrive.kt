package frc.team6502.robot.commands

import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.PIDCommand
import frc.team6502.robot.RobotMap

class DefaultDrive : PIDCommand(RobotMap.driveStraightPID.p, RobotMap.driveStraightPID.i, RobotMap.driveStraightPID.d) {

    var correction = 0.0
    var drivingStraight = false

    init {
        requires(RobotMap.kDrivetrain)
    }

    override fun initialize() {
        println("Drive initialized :)")
    }

    override fun execute() {
        // if driving straight
            // apply correction
        // else if not driving straight and commanded to
            // zero gyro, start correcting
        // drive
    }

    override fun isFinished(): Boolean {
        return false
    }

    // TODO('define curvaturedrive')

    override fun usePIDOutput(output: Double) {
        correction = output
    }

    override fun returnPIDInput(): Double {
        // do gyro stuff
        return 0.0
    }

}