package frc.team6502.robot

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commands.DefaultDrive
import frc.team6502.robot.subsystems.Drivetrain

class Robot : TimedRobot() {

    private val chooser = SendableChooser<String>()

    override fun robotInit() {
        RobotMap // lazy init all the RobotMap vars
        Drivetrain // create the drive boi

        println("Hello, 2019 season!")

        // setup auto chooser
        chooser.name = "Station Position"
        chooser.addOption("Left", "LEFT")
        chooser.addOption("Center", "CENTER")
        chooser.addOption("Right", "RIGHT")

        SmartDashboard.putData(chooser)
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
