package frc.team6502.robot

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commands.vision.SetLEDRing
import frc.team6502.robot.sensor.RobotOdometry
import frc.team6502.robot.subsystems.*

class Robot : TimedRobot(TIMESTEP) {

    private val autoChooser = SendableChooser<Command?>()
    private var autoCommand: Command? = null

    private val startingGamePiece = GamePiece.NONE

    override fun robotInit() {
        // report kotlin as the language (unofficially)
        val kLanguageKotlin = 6
        HAL.report(FRCNetComm.tResourceType.kResourceType_Language, kLanguageKotlin)

        // create all the util classes
        RobotMap
        RobotOdometry
        RobotStatus
        Modes

        // create subsystems
        Drivetrain
        Elevator
        HatchPanelIntake
        CargoIntake
//        Lighting

        RobotMap.kCompressor.closedLoopControl = true
//        RobotMap.kJevois
//        RobotMap.kJevois.setCam("absexp","500")

//        SmartDashboard.putData(CharacterizeDrivetrain())

        autoChooser.addOption("Hybrid", null)
    }

    override fun disabledInit() {
        OI.createElevatorButtons()
        SmartDashboard.putBoolean("Correcting", false)
        SmartDashboard.putNumber("Heading Correction", 0.0)
//        Elevator.setpoint = 0.0
//        Elevator.elevatorTalon.set(ControlMode.Position, 0.0)
        SetLEDRing(false).start()
        Wedges.deployed = false
        RobotStatus.setGamePiece(startingGamePiece)
    }

    /**
     * Do not delete me or bad things will happen.
     */


    override fun autonomousInit() {
        RobotOdometry.zero()
        Elevator.updateSetpoint()

        /*println("Generating spline")
        val cfg = Trajectory.Config(Trajectory.FitMethod.HERMITE_QUINTIC, Trajectory.Config.SAMPLES_LOW, TIMESTEP, 5.0, 2.0, 18.0)
         val waypoints = arrayOf(
                 Waypoint(0.0,0.0, Pathfinder.d2r(0.0)),
                 Waypoint(5.05, 0.471, -0.08)
         )
         val t = Pathfinder.generate(waypoints, cfg)
        Pathfinder.writeToCSV(File("/U/traj.csv"), t)
         println("Running path")
        autoCommand = RamseteFollowPath(t, 0.7, 0.2)
        autoCommand?.start()*/
        autoCommand = autoChooser.selected
        autoCommand?.start()
    }

    override fun teleopInit() {
        // if auto is still running for some reason, stop it
        autoCommand?.cancel()

        // zero everything
        RobotOdometry.zero()
        Elevator.elevatorTalon.selectedSensorPosition = 0

        Elevator.updateSetpoint()

        // make elevator go to level 1 idle height
        OI.setElevatorHeight(0)
    }

    override fun robotPeriodic() {
        // do everything
//        Scheduler.getInstance().run()
        Scheduler.getInstance().run()

        OI.poll()

        SmartDashboard.putNumber("elev height", Elevator.elevatorTalon.selectedSensorPosition.toDouble())
        SmartDashboard.putNumber("elev error", Elevator.elevatorTalon.closedLoopError.toDouble())
    }

    override fun teleopPeriodic() {}
    override fun autonomousPeriodic() {}

    override fun disabledPeriodic() {
        Elevator.elevatorTalon.set(ControlMode.Position, Elevator.elevatorTalon.selectedSensorPosition.toDouble())
    }

}
