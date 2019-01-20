package frc.team6502.robot.subsystems

import com.ctre.phoenix.motorcontrol.ControlMode
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX
import edu.wpi.first.wpilibj.command.Subsystem
import frc.team6502.kyberlib.drive.IDrivetrain
import frc.team6502.robot.RobotMap

object Drivetrain :  Subsystem() {

    val leftTalon = WPI_TalonSRX(0)


    init {
        // probably going to be a wcd
        // two ktalon objects needed, config tbd
        for(id in RobotMap.leftVictorIds){
            WPI_VictorSPX(id).follow(leftTalon)
        }

        println("Made a thing")
    }

    fun setDrivePercentages(left: Double, right: Double){

    }

    fun setDriveVelocities(left: Double, right: Double){

    }

    fun setBrake(brake: Boolean){

    }

    override fun initDefaultCommand() {
        defaultCommand = null
    }

}