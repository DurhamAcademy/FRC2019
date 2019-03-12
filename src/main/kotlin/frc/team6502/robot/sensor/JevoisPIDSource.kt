package frc.team6502.robot.sensor

import edu.wpi.first.wpilibj.PIDSource
import edu.wpi.first.wpilibj.PIDSourceType
import frc.team6502.robot.JEVOIS_OFFSET
import frc.team6502.robot.RobotMap

class JevoisPIDSource : PIDSource {
    override fun getPIDSourceType(): PIDSourceType {
        return PIDSourceType.kDisplacement
    }

    override fun setPIDSourceType(pidSource: PIDSourceType?) {

    }

    override fun pidGet(): Double {
        return if (RobotMap.kJevois.data.getOrDefault("hasContour", false) as Boolean) {
            (RobotMap.kJevois.data.getOrDefault("x", 0.0) as Double) - JEVOIS_OFFSET.inches
        } else {
            0.0
        }

    }

}