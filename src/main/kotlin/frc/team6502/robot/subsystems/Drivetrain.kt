package frc.team6502.robot.subsystems

import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.kyberlib.drive.IDrivetrain

class Drivetrain : IDrivetrain, Subsystem() {

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

    override fun initDefaultCommand() {
        defaultCommand = null
    }

}