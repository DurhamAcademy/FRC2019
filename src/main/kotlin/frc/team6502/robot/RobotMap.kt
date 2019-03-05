package frc.team6502.robot

import com.ctre.phoenix.sensors.PigeonIMU
import edu.wpi.first.wpilibj.Compressor
import edu.wpi.first.wpilibj.DigitalOutput
import frc.team6502.robot.sensor.Jevois

/**
 * This contains actual robot components and indices
 */
object RobotMap {

    // PORTS //
    val leftTalonId = 0
    val leftVictorIds = arrayOf(1,2)

    val rightTalonId = 3
    val rightVictorIds = arrayOf(4,5)

    val imuId = 6

    val elevatorTalonId = 7
    val elevatorVictorIds = arrayOf(8, 9, 10)


    val intakeTalonId = 11
    val intakeVictorId = 12
    val frontIntakeTalonId = 13

    // SENSORS //
    val kJevois = Jevois(true)
    val kLEDRingRelay = DigitalOutput(0)

    val kIMU = PigeonIMU(imuId)

    val kCompressor = Compressor(0)

    val hatchSolenoidId = 0
    val rampSolenoidIds = arrayOf(1, 2)
    val wedgeSolenoidIds = arrayOf(3, 4)
}