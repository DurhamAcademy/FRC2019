package frc.team6502.robot.sensor

import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.PIDSource
import edu.wpi.first.wpilibj.PIDSourceType

class VisionPIDSource : PIDSource {
    val lt = NetworkTableInstance.getDefault().getTable("limelight")
    override fun getPIDSourceType(): PIDSourceType {
        return PIDSourceType.kDisplacement
    }

    override fun setPIDSourceType(pidSource: PIDSourceType?) {

    }

    override fun pidGet(): Double {
        return if (lt.getEntry("tv").getNumber(0) as Int > 0)
            lt.getEntry("tx").getDouble(0.0)
        else 0.0
    }

}