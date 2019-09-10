package frc.team6502.robot

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.buttons.JoystickButton
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commandgroups.DeployWedges
import frc.team6502.robot.commands.manip.*
import frc.team6502.robot.subsystems.Elevator
import java.lang.Math.abs
import kotlin.math.pow

object OI {
    val controller = XboxController(0)

    /**
     * The currently selected elevator height
     */
    var selectedElevatorHeight = 0
        private set

    /**
     * Sets the elevator's desired level (0 - 2) -> (Level 1 - Level 3) and updates the dashboard accordingly
     */
    fun setElevatorHeight(index: Int) {
        if (index !in 0 until HEIGHTS.size) return
        println("SET TO $index")

        // if you are intaking, stop it
        if (index > 0) IntakeCargo.singleton?.cancel()

        // update backing field
        selectedElevatorHeight = index

        // get height pair
        val height = HEIGHTS[index]

        // set elevator buttons back to false
        createElevatorButtons()

        // put the boolean
        SmartDashboard.putBoolean(height.second, true)

        // move the elevator to the correct point
        SetElevatorHeight(height.first).start()
        println("Set height to ${height.first.feet}ft")
    }

    val commandingStraight: Boolean
        get() = !commandedVC && deadband(controller.x, 0.1) == 0.0

    val commandedY: Double
        get() = deadband(controller.getY(GenericHID.Hand.kLeft), 0.05).pow(3) * if(controller.getTriggerAxis(GenericHID.Hand.kLeft) > 0.5) 1.0 else (40/(Elevator.height * 12 + 40))

    val commandedX: Double
        get() = deadband(controller.getX(GenericHID.Hand.kRight), 0.05).pow(3) * 0.25
    val commandedVC: Boolean
        get() = controller.getTriggerAxis(GenericHID.Hand.kRight) > 0.2

    /**
     * Applies a deadband to an input
     * @param input The value to apply the deadband to
     * @param deadband The minimum value required for the return value to be non-zero
     */
    fun deadband(input: Double, deadband: Double): Double {
        return if (abs(input) < deadband) 0.0 else input
    }

    /**
     * Set the controller rumble
     */
    fun setControllerRumble(rumble: Double) {
        controller.setRumble(GenericHID.RumbleType.kLeftRumble, rumble)
        controller.setRumble(GenericHID.RumbleType.kRightRumble, rumble)
    }

    /**
     * Change elevator height if buttons indicate it should change
     */
    fun poll() {
        // poll physical buttons
        if (controller.getRawButtonPressed(5)) {
            setElevatorHeight((selectedElevatorHeight - 1))
        }
        if (controller.getRawButtonPressed(6)) {
            setElevatorHeight((selectedElevatorHeight + 1))
        }

        // poll digital buttons
        for (idx in HEIGHTS.indices) {
            val height = HEIGHTS[idx]
            if (!SmartDashboard.getBoolean(height.second, false) && selectedElevatorHeight == idx) {
                SmartDashboard.putBoolean(height.second, true)
            }
            if (SmartDashboard.getBoolean(height.second, false) && selectedElevatorHeight != idx) {
                setElevatorHeight(idx)
            }
        }

        if (SmartDashboard.getBoolean("CargoStatusNone", false)) RobotStatus.setStatusCargo(CargoStatus.NONE)
        if (SmartDashboard.getBoolean("CargoStatusIdle", false)) RobotStatus.setStatusCargo(CargoStatus.IDLE)
        if (SmartDashboard.getBoolean("CargoStatusArmedShip", false)) RobotStatus.setStatusCargo(CargoStatus.ARMED_SHIP)
        if (SmartDashboard.getBoolean("CargoStatusArmedRocket", false)) RobotStatus.setStatusCargo(CargoStatus.ARMED_ROCKET)

        if (SmartDashboard.getBoolean("HatchStatusNone", false)) RobotStatus.setStatusHatch(HatchStatus.NONE)
        if (SmartDashboard.getBoolean("HatchStatusArmed", false)) RobotStatus.setStatusHatch(HatchStatus.ARMED)

    }

    /**
     * Put elevator status buttons on the dashboard
     */
    fun createElevatorButtons() {
        for (height in HEIGHTS) {
            SmartDashboard.putBoolean(height.second, false)
        }
    }

    init {

        // A (1) - Cancel
        // B (2) - Ball
        // X (3) - Panel
        // Y (4) - Cargo ship ball
        // LB (5) - Cycle down
        // RB (6) - Cycle up
        JoystickButton(controller, 1).whenPressed(CancelOperation())
        JoystickButton(controller, 2).whenPressed(ManipulateCargo(false))
        JoystickButton(controller, 3).whenPressed(ManipulatePanel())
        JoystickButton(controller, 4).whenPressed(ManipulateCargo(true))
        JoystickButton(controller, 7).whenPressed(DeployWedges())
    }
}