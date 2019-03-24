package frc.team6502.robot

import com.ctre.phoenix.motorcontrol.ControlMode
import edu.wpi.first.cameraserver.CameraServer
import edu.wpi.first.hal.FRCNetComm
import edu.wpi.first.hal.HAL
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.TimedRobot
import edu.wpi.first.wpilibj.command.Command
import edu.wpi.first.wpilibj.command.Scheduler
import edu.wpi.first.wpilibj.livewindow.LiveWindow
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commands.drive.CharacterizeDrivetrain
import frc.team6502.robot.commands.drive.RamseteFollowPath
import frc.team6502.robot.sensor.RobotOdometry
import frc.team6502.robot.subsystems.*
import java.io.File

class Robot : TimedRobot(TIMESTEP) {

    private val autoChooser = SendableChooser<Command?>()
    private var autoCommand: Command? = null

    private val startingGamePiece = GamePiece.HATCH

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

        SmartDashboard.putData(CharacterizeDrivetrain())

//        CameraServer.getInstance().startAutomaticCapture()

        autoChooser.addOption("Hybrid", null)
        File("/home/lvuser/deploy/paths").listFiles().forEach {
            if (!it.name.endsWith(".left.pf1.csv") && !it.name.endsWith(".right.pf1.csv"))
                autoChooser.addOption(it.name.replace(".pf1.csv", ""), RamseteFollowPath(it.name.replace(".pf1.csv", ""), B, ZETA, 1.5))
        }

        SmartDashboard.putData("Auto",autoChooser)
        LiveWindow.disableAllTelemetry()

        // zero elevator height on boot
        Elevator.elevatorTalon.selectedSensorPosition = 0
    }

    override fun disabledInit() {

        OI.setElevatorHeight(0)
        OI.createElevatorButtons()

        SmartDashboard.putBoolean("Correcting", false)
        SmartDashboard.putNumber("Heading Correction", 0.0)


//        Elevator.setpoint = 0.0
//        Elevator.elevatorTalon.set(ControlMode.Position, 0.0)

        RobotStatus.setGamePiece(startingGamePiece)
    }

    /**
     * Do not delete me or bad things will happen.
     */


    override fun autonomousInit() {
        RobotOdometry.zero()
        Elevator.updateSetpoint()
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0)
        autoCommand = autoChooser.selected
        autoCommand?.start()
    }

    override fun teleopInit() {
        // if auto is still running for some reason, stop it
        autoCommand?.cancel()
        Elevator.updateSetpoint()
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0)
        // make elevator go to level 1

        //TODO: if autos are going to level 2+ remove this!!!
        OI.setElevatorHeight(0)
    }

    override fun robotPeriodic() {
        Scheduler.getInstance().run()

        OI.poll()

        SmartDashboard.putBoolean("Has None", RobotStatus.currentGamePiece == GamePiece.NONE)
        SmartDashboard.putBoolean("Has Cargo", RobotStatus.currentGamePiece == GamePiece.CARGO)
        SmartDashboard.putBoolean("Has Panel", RobotStatus.currentGamePiece == GamePiece.HATCH)

        //TODO investigate this
//        SmartDashboard.putNumber("elev height", Elevator.elevatorTalon.selectedSensorPosition.toDouble())
//        SmartDashboard.putNumber("elev error", Elevator.elevatorTalon.closedLoopError.toDouble())
    }

    override fun teleopPeriodic() {

        Wedges.unlock = true
    }
    override fun autonomousPeriodic() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(0)
    }

    override fun disabledPeriodic() {
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(if(OI.commandedVC) 0 else 1)
        NetworkTableInstance.getDefault().getTable("limelight").getEntry("ledMode").setNumber(if(OI.commandedVC) 0 else 1)
        // make elevator not go sicko mode on enable (constantly set setpoint to current position)
        Elevator.elevatorTalon.set(ControlMode.Position, Elevator.elevatorTalon.selectedSensorPosition.toDouble())
    }

}
