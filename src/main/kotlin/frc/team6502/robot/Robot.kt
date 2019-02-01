package frc.team6502.robot

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.kyberlib.util.units.degrees
import frc.team6502.robot.commands.CharacterizeDrivetrain
import frc.team6502.robot.sensor.RobotOdometry
import frc.team6502.robot.subsystems.Drivetrain
import frc.team6502.robot.subsystems.Intake

class Robot : TimedRobot() {

    private val chooser = SendableChooser<String>()

    override fun robotInit() {

        // report kotlin as the language (unofficially)
        val kLanguageKotlin = 6
        HAL.report(FRCNetComm.tResourceType.kResourceType_Language, kLanguageKotlin)

        RobotMap // lazy init all the RobotMap vars
        Modes // sicko mode
        Drivetrain // create the drive boi
        Intake

        SmartDashboard.putData(CharacterizeDrivetrain())

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
        /* println("Generating spline")
         val cfg = Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_FAST, 0.05, 5.0, 2.0, 18.0)
         val waypoints = arrayOf(
                 Waypoint(0.0,0.0, Pathfinder.d2r(0.0)),
                 Waypoint(5.0, 0.0, 0.0)
         )
         val t = Pathfinder.generate(waypoints, cfg)
         println("Running path")
         RamseteFollowPath(t, 3.0, 0.7).start()*/
    }

    override fun autonomousPeriodic() {}

    override fun teleopInit() {
        RobotOdometry.zero()
    }

    override fun teleopPeriodic() {
        RobotOdometry.addPose(RobotMap.kIMU.getYaw().degrees, (Drivetrain.getVelocities().first + Drivetrain.getVelocities().second) / 2.0)
        SmartDashboard.putNumber("x", RobotOdometry.odometry.pose.x.feet)
        SmartDashboard.putNumber("y", RobotOdometry.odometry.pose.y.feet)
        SmartDashboard.putNumber("theta", RobotOdometry.odometry.pose.theta.degrees)
    }

    override fun testInit() {}

    override fun testPeriodic() {}

}
