package frc.team6502.robot

import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commands.drive.CharacterizeDrivetrain
import frc.team6502.robot.commands.drive.RamseteFollowPath
import frc.team6502.robot.commands.vision.SetLEDRing
import frc.team6502.robot.sensor.RobotOdometry
import frc.team6502.robot.subsystems.*
import jaci.pathfinder.*
import java.io.File

class Robot : TimedRobot() {

    private val chooser = SendableChooser<String>()
    private var autoCommand: Command? = null

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
        RobotMap.kJevois.setStreaming(true)
//        RobotMap.kJevois.setCam("absexp","500")

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
//        Elevator.setpoint = 0.0
//        Elevator.elevatorTalon.set(ControlMode.Position, 0.0)
        SetLEDRing(false).start()
    }

    /**
     * Do not delete me or bad things will happen.
     */
    override fun robotPeriodic() {
        // do everything
//        Scheduler.getInstance().run()
        SmartDashboard.putNumber("x", RobotOdometry.odometry.pose.x.feet)
        SmartDashboard.putNumber("y", RobotOdometry.odometry.pose.y.feet)
        SmartDashboard.putNumber("theta", RobotOdometry.odometry.pose.theta.degrees)
        OI.pollElevatorButtons()
        SmartDashboard.putNumber("height", Elevator.elevatorTalon.selectedSensorPosition.toDouble())
        SmartDashboard.putNumber("elev error", Elevator.elevatorTalon.closedLoopError.toDouble())
        LED()
    }

    override fun autonomousInit() {
        RobotOdometry.zero()

        println("Generating spline")
        val cfg = Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_LOW, RobotMap.TIMESTEP, 5.0, 2.0, 18.0)
         val waypoints = arrayOf(
                 Waypoint(0.0,0.0, Pathfinder.d2r(0.0)),
                 Waypoint(5.05, 0.471, -0.08)
         )
         val t = Pathfinder.generate(waypoints, cfg)
        Pathfinder.writeToCSV(File("/U/traj.csv"), t)
         println("Running path")
        autoCommand = RamseteFollowPath(t, 0.7, 0.2)
        autoCommand?.start()
    }

    override fun autonomousPeriodic() {
//        RobotOdometry.addPose(RobotMap.kIMU.getYaw().degrees, (Drivetrain.getVelocities().first + Drivetrain.getVelocities().second) / 2.0)
        Scheduler.getInstance().run()
        SmartDashboard.putNumber("x", RobotOdometry.odometry.pose.x.feet)
        SmartDashboard.putNumber("y", RobotOdometry.odometry.pose.y.feet)
        SmartDashboard.putNumber("theta", RobotOdometry.odometry.pose.theta.degrees)
    }

    override fun teleopInit() {
        autoCommand?.cancel()
        RobotOdometry.zero()
//        Elevator.zeroHeight()
        Elevator.elevatorTalon.selectedSensorPosition = 0
//        Elevator.elevatorTalon.set(ControlMode.Position, 0.0)
//        Elevator.setpoint = 0.0
        OI.setElevatorHeight(0)
    }

    override fun teleopPeriodic() {
        Scheduler.getInstance().run()
        if (OI.controller.getRawButtonPressed(5)) {
//            println("before: ${OI.selectedElevatorHeight}")
            OI.setElevatorHeight((OI.selectedElevatorHeight - 1))
//            println("after: ${OI.selectedElevatorHeight}")
        }
        if (OI.controller.getRawButtonPressed(6)) {
//            println("before: ${OI.selectedElevatorHeight}")
            OI.setElevatorHeight((OI.selectedElevatorHeight + 1))
//            println("after: ${OI.selectedElevatorHeight}")
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
