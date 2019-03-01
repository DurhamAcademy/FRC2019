package frc.team6502.robot

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.buttons.JoystickButton
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commands.lighting.RequestCargo
import frc.team6502.robot.commands.lighting.RequestPanel
import frc.team6502.robot.commands.manip.*
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
        IntakeCargo.singleton?.cancel()
        selectedElevatorHeight = index
        val height = HEIGHTS[index]
        createElevatorButtons()
        SmartDashboard.putBoolean(height.second, true)
        SetElevatorHeight(height.first).start()
        println("Set height to ${height.first.feet}ft")
    }

    val commandingStraight: Boolean
        get() = deadband(controller.x, 0.1) == 0.0

    val commandedY: Double
        get() = deadband(controller.getY(GenericHID.Hand.kLeft).pow(3), 0.04)

    val commandedX: Double
        get() = deadband(controller.getX(GenericHID.Hand.kRight).pow(3), 0.04)

    val commandedVC: Double
        get() = controller.getTriggerAxis(GenericHID.Hand.kRight)

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

        // if dashboard says i have cargo when i dont think i do
        if (SmartDashboard.getBoolean("Has Cargo", false) && RobotStatus.currentGamePiece != GamePiece.CARGO)
            RobotStatus.setGamePiece(GamePiece.CARGO)

        // if dashboard says shouldnt have cargo when i think i do
        if (!SmartDashboard.getBoolean("Has Cargo", true) && RobotStatus.currentGamePiece == GamePiece.CARGO)
            RobotStatus.setGamePiece(GamePiece.NONE)

        // if dashboard says i have panel when i dont think i do
        if (SmartDashboard.getBoolean("Has Panel", false) && RobotStatus.currentGamePiece != GamePiece.HATCH)
            RobotStatus.setGamePiece(GamePiece.HATCH)

        // if dashboard says shouldnt have panel when i think i do
        if (!SmartDashboard.getBoolean("Has Panel", true) && RobotStatus.currentGamePiece == GamePiece.HATCH)
            RobotStatus.setGamePiece(GamePiece.NONE)

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

        // A (1) - Align
        // B (2) - Ball
        // X (3) - Panel
        // Y (4) - Cancel
        // LB (5) - Cycle down
        // RB (6) - Cycle up
        JoystickButton(controller, 2).whenPressed(ManipulateCargo(true))
        JoystickButton(controller, 2).whenPressed(ManipulateCargo(false))
        JoystickButton(controller, 3).whenPressed(ManipulatePanel())
        JoystickButton(controller, 4).whenPressed(CancelOperation())
        JoystickButton(controller, 7).whenPressed(RequestPanel())
        JoystickButton(controller, 8).whenPressed(RequestCargo())
    }
}