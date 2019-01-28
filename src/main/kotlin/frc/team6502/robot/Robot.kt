package frc.team6502.robot

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.subsystems.Drivetrain
import frc.team6502.robot.subsystems.Intake

class Robot : TimedRobot() {

    private val chooser = SendableChooser<String>()

    override fun robotInit() {

        // report kotlin as the language (unofficially)
        val kLanguageKotlin = 6
        HAL.report(FRCNetComm.tResourceType.kResourceType_Language, kLanguageKotlin)

        RobotMap // lazy init all the RobotMap vars
        Drivetrain // create the drive boi
        Intake

        println("Hello, 2019 season!")

        // setup auto chooser
        chooser.name = "Robot Position"
        chooser.addOption("Left", "LEFT")
        chooser.addOption("Center", "CENTER")
        chooser.addOption("Right", "RIGHT")

        SmartDashboard.putData(chooser)
    }

    override fun disabledInit() {
        SmartDashboard.putBoolean("Correcting", false)
        SmartDashboard.putNumber("Heading Correction", 0.0)
    }

    /**
     * Do not delete me or bad things will happen.
     */
    override fun robotPeriodic() {
        // do everything
        Scheduler.getInstance().run()
    }

    override fun autonomousInit() {
        println("Selected autonomous: ${chooser.selected}")
    }

    override fun autonomousPeriodic() {}

    override fun teleopInit() {}

    override fun teleopPeriodic() {}

    override fun testInit() {}

    override fun testPeriodic() {}

}
