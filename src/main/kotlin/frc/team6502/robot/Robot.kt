package frc.team6502.robot

import com.ctre.phoenix.motorcontrol.ControlMode
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

    private val preloadChooser = SendableChooser<GamePiece>()

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

        autoChooser.setDefaultOption("Hybrid", null)
        File("/home/lvuser/deploy/paths").listFiles().forEach {
            if (!it.name.endsWith(".left.pf1.csv") && !it.name.endsWith(".right.pf1.csv"))
                autoChooser.addOption(it.name.replace(".pf1.csv", ""), RamseteFollowPath(it.name.replace(".pf1.csv", ""), B, ZETA, 1.5))
        }

        preloadChooser.setDefaultOption("None", GamePiece.NONE)
        preloadChooser.addOption("Hatch", GamePiece.HATCH)
        preloadChooser.addOption("Cargo", GamePiece.CARGO)

        SmartDashboard.putData("Auto",autoChooser)
        SmartDashboard.putData("Preload", preloadChooser)

        SmartDashboard.setPersistent("Auto")
        SmartDashboard.setPersistent("Preload")

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

    }

    /**
     * Do not delete me or bad things will happen.
     */


    override fun autonomousInit() {
        RobotOdometry.zero()
        when (preloadChooser.selected) {
            GamePiece.NONE -> {
                RobotStatus.setStatusCargo(CargoStatus.NONE)
                RobotStatus.setStatusHatch(HatchStatus.NONE)
            }
            GamePiece.CARGO -> {
                RobotStatus.setStatusCargo(CargoStatus.IDLE)
            }
            GamePiece.HATCH -> {
                RobotStatus.setStatusHatch(HatchStatus.ARMED)
            }
            else -> {
            }
        }
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

        SmartDashboard.putBoolean("IsCargoStatusNone", RobotStatus.cargoStatus == CargoStatus.NONE)
        SmartDashboard.putBoolean("IsCargoStatusIdle", RobotStatus.cargoStatus == CargoStatus.IDLE)
        SmartDashboard.putBoolean("IsCargoStatusArmedShip", RobotStatus.cargoStatus == CargoStatus.ARMED_SHIP)
        SmartDashboard.putBoolean("IsCargoStatusArmedRocket", RobotStatus.cargoStatus == CargoStatus.ARMED_ROCKET)

        SmartDashboard.putBoolean("IsHatchStatusNone", RobotStatus.hatchStatus == HatchStatus.NONE)
        SmartDashboard.putBoolean("IsHatchStatusArmed", RobotStatus.hatchStatus == HatchStatus.ARMED)

        //TODO investigate this
        SmartDashboard.putNumber("elev height", Elevator.elevatorTalon.selectedSensorPosition.toDouble())
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
