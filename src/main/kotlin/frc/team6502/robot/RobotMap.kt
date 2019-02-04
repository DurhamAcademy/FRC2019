package frc.team6502.robot

import com.ctre.phoenix.sensors.PigeonIMU
import edu.wpi.cscore.VideoMode
import edu.wpi.first.wpilibj.DigitalOutput
import frc.team6502.kyberlib.motorcontrol.PIDConfig
import frc.team6502.kyberlib.vision.Jevois

object RobotMap {

    // PORTS //
    val leftTalonId = 0
    val leftVictorIds = arrayOf(1,2)

    val rightTalonId = 3
    val rightVictorIds = arrayOf(4,5)

    val imuId = 6

    val elevatorTalonId = 7
    val elevatorVictorIds = arrayOf(8, 9, 10)
//    val elevtatorVictorIds = arrayOf(8, 9, 10)

    // SENSORS //
    // TODO('update stream format')
    val jevoisVideoMode = VideoMode(VideoMode.PixelFormat.kYUYV, 322, 288, 30)
    val kJevois = Jevois(streamInfo = jevoisVideoMode)
    val kLEDRingRelay = DigitalOutput(0)

    val kIMU = PigeonIMU(imuId)

    // TUNING //
    val driveStraightPID = PIDConfig(0.0, 0.0, 0.0)
    val elevatorPID      = PIDConfig(0.0, 0.0, 0.0)

    // AUTOS //

}