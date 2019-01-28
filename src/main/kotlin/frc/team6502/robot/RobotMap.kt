package frc.team6502.robot

import com.ctre.phoenix.sensors.PigeonIMU
import frc.team6502.kyberlib.motorcontrol.PIDConfig

object RobotMap {

    // PORTS //
    val leftTalonId = 0
    val leftVictorIds = arrayOf(1,2)

    val rightTalonId = 3
    val rightVictorIds = arrayOf(4,5)

    val imuId = 6

    val intakeVictorID = 7
//    val elevtatorVictorIds = arrayOf(8, 9, 10)

    // SENSORS //
    // TODO('update stream format')
//    val kJevois = Jevois()
    val kIMU = PigeonIMU(imuId)

    // TUNING //
    val driveStraightPID = PIDConfig(0.0, 0.0, 0.0)
    val elevatorPID      = PIDConfig(0.0, 0.0, 0.0)

    // AUTOS //

}