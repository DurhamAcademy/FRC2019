package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.command.PIDSubsystem
import frc.team6502.kyberlib.drive.IDrivetrain
import frc.team6502.robot.commands.DefaultDrive

class Drivetrain() : IDrivetrain, PIDSubsystem(0.0, 0.0, 0.0) {

    init {
        // probably going to be a wcd
        // two ktalon objects needed, config tbd
        println("Made a thing")
    }

    override fun getDriveVelocities(): Pair<Double, Double> {
        TODO("not implemented")
    }

    override fun setDriveVelocities(left: Double, right: Double) {
        TODO("not implemented")
    }

    override fun usePIDOutput(output: Double) {
        // set correction
        TODO("not implemented")
    }

    override fun returnPIDInput(): Double {
        // calculate gyro error
        TODO("not implemented")
    }

    override fun initDefaultCommand() {
        defaultCommand = DefaultDrive()
    }

}