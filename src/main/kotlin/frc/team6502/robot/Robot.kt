package frc.team6502.robot

import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Scheduler
import frc.team6502.robot.commands.DefaultDrive

class Robot : TimedRobot() {

    override fun robotInit() {
        RobotMap // lazy init all the RobotMap vars

        println("Hello, 2019 season!")
    }

    /**
     * Do not delete me or bad things will happen.
     */
    override fun robotPeriodic() {
        Scheduler.getInstance().run()
    }

    override fun autonomousInit() {}

    override fun autonomousPeriodic() {}

    override fun teleopInit() {}

    override fun teleopPeriodic() {}

    override fun testInit() {}

    override fun testPeriodic() {}

}
