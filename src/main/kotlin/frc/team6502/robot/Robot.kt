package frc.team6502.robot

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commands.drive.CharacterizeDrivetrain
import frc.team6502.robot.commands.drive.RamseteFollowPath
import frc.team6502.robot.sensor.RobotOdometry
import frc.team6502.robot.subsystems.CargoIntake
import frc.team6502.robot.subsystems.Drivetrain
import frc.team6502.robot.subsystems.Elevator
import frc.team6502.robot.subsystems.HatchPanelIntake
import jaci.pathfinder.Pathfinder
import jaci.pathfinder.Trajectory
import jaci.pathfinder.Waypoint

class Robot : TimedRobot() {

    private val chooser = SendableChooser<String>()


    override fun robotInit() {

        // report kotlin as the language (unofficially)
        val kLanguageKotlin = 6
        HAL.report(FRCNetComm.tResourceType.kResourceType_Language, kLanguageKotlin)

        RobotMap // lazy init all the RobotMap vars
        RobotOdometry
        Modes // sicko mode
        Drivetrain // create the drive boi
        Elevator
        HatchPanelIntake
        CargoIntake

        RobotMap.kCompressor.closedLoopControl = true

        SmartDashboard.putData(CharacterizeDrivetrain())
        SmartDashboard.putBoolean("Has Panel", false)


//        // setup auto chooser
//        chooser.name = "Robot Position"
//        chooser.addOption("Left", "LEFT")
//        chooser.addOption("Center", "CENTER")
//        chooser.addOption("Right", "RIGHT")
//
//        SmartDashboard.putData(chooser)
    }

    override fun disabledInit() {
        OI.createElevatorButtons()
        SmartDashboard.putBoolean("Correcting", false)
        SmartDashboard.putNumber("Heading Correction", 0.0)
    }

    /**
     * Do not delete me or bad things will happen.
     */
    override fun robotPeriodic() {
        // do everything
//        Scheduler.getInstance().run()
        OI.pollElevatorButtons()
        SmartDashboard.putNumber("height", Elevator.elevatorTalon.selectedSensorPosition.toDouble())
        SmartDashboard.putNumber("elev error", Elevator.elevatorTalon.closedLoopError.toDouble())
    }

    override fun autonomousInit() {
        RobotOdometry.zero()

        println("Generating spline")
        val cfg = Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_LOW, RobotMap.TIMESTEP, 3.0, 1.0, 18.0)
         val waypoints = arrayOf(
                 Waypoint(0.0,0.0, Pathfinder.d2r(0.0)),
                 Waypoint(10.0, 10.0, 0.0)
         )
         val t = Pathfinder.generate(waypoints, cfg)
         println("Running path")
        RamseteFollowPath(t, 2.0, 0.4).start()
    }

    override fun autonomousPeriodic() {
//        RobotOdometry.addPose(RobotMap.kIMU.getYaw().degrees, (Drivetrain.getVelocities().first + Drivetrain.getVelocities().second) / 2.0)
        Scheduler.getInstance().run()
        SmartDashboard.putNumber("x", RobotOdometry.odometry.pose.x.feet)
        SmartDashboard.putNumber("y", RobotOdometry.odometry.pose.y.feet)
        SmartDashboard.putNumber("theta", RobotOdometry.odometry.pose.theta.degrees)
    }

    override fun teleopInit() {
        RobotOdometry.zero()
        OI.selectedElevatorHeight = 0
        Elevator.zeroHeight()
    }

    override fun teleopPeriodic() {
        Scheduler.getInstance().run()
        if (OI.controller.getRawButton(5)) {
            OI.selectedElevatorHeight = (OI.selectedElevatorHeight - 1) % RobotMap.heights.size
        }
        if (OI.controller.getRawButton(6)) {
            OI.selectedElevatorHeight = (OI.selectedElevatorHeight + 1) % RobotMap.heights.size
        }
//        RobotOdometry.addPose(RobotMap.kIMU.getYaw().degrees, (Drivetrain.getVelocities().first + Drivetrain.getVelocities().second) / 2.0)
//        SmartDashboard.putNumber("x", RobotOdometry.odometry.pose.x.feet)
//        SmartDashboard.putNumber("y", RobotOdometry.odometry.pose.y.feet)
//        SmartDashboard.putNumber("theta", RobotOdometry.odometry.pose.theta.degrees)
//        SmartDashboard.putNumber("elevator error", Elevator.elevatorTalon.closedLoopError.toDouble())
    }

    override fun testInit() {}

    override fun testPeriodic() {}

}
