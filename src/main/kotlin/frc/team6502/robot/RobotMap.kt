package frc.team6502.robot

import frc.team6502.kyberlib.motorcontrol.PIDConfig
import frc.team6502.robot.subsystems.Drivetrain

object RobotMap {

    // PORTS //

    // SUBSYSTEMS //
    val kDrivetrain = Drivetrain()
    // val kElevator = Elevator()

    // SENSORS //
    // TODO('update stream format')
    // val kJevois = Jevois()

    // TUNING //
    val driveStraightPID = PIDConfig(0.0, 0.0, 0.0)
    val elevatorPID      = PIDConfig(0.0, 0.0, 0.0)

    // AUTOS //

}