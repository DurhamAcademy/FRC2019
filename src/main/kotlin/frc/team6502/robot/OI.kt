package frc.team6502.robot

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.buttons.JoystickButton
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.robot.commandgroups.VisionAlign
import frc.team6502.robot.commands.manip.*
import frc.team6502.robot.commands.vision.SetLEDRing
import java.lang.Math.abs
import kotlin.math.pow

object OI {
    val controller = XboxController(0)
    var selectedElevatorHeight = 0
        private set

    fun setElevatorHeight(index: Int) {
        if (index !in 0..2) return
        println("SET TO $index")
        selectedElevatorHeight = index
        val height = RobotMap.heights[index]
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

    fun deadband(input: Double, deadband: Double): Double {
        return if (abs(input) < deadband) 0.0 else input
    }

    fun setControllerRumble(rumble: Double) {
        controller.setRumble(GenericHID.RumbleType.kLeftRumble, rumble)
        controller.setRumble(GenericHID.RumbleType.kRightRumble, rumble)
    }

    fun pollElevatorButtons() {
        for (idx in RobotMap.heights.indices) {

            val height = RobotMap.heights[idx]
            if (!SmartDashboard.getBoolean(height.second, false) && selectedElevatorHeight == idx) {
                SmartDashboard.putBoolean(height.second, true)
            }
            if (SmartDashboard.getBoolean(height.second, false) && selectedElevatorHeight != idx) {
                selectedElevatorHeight = idx
            }
        }
    }


    fun createElevatorButtons() {
        for (height in RobotMap.heights) {
            SmartDashboard.putBoolean(height.second, false)
        }
    }

    init {

        // A (1) - Align
        // B (2) - Ball
        // X (3) - Panel
        // Y (4) - Cancel
        // LB (5)- Cycle down
        // RB (6) - Cycle up

        JoystickButton(controller, 1).whenPressed(VisionAlign())
        JoystickButton(controller, 2).whenPressed(IntakeCargo())
        JoystickButton(controller, 3).whenPressed(ManipulatePanel())
        JoystickButton(controller, 4).whenPressed(CancelOperation())
        JoystickButton(controller, 7).whenPressed(SetLEDRing(true))
        JoystickButton(controller, 8).whenPressed(SetLEDRing(false))
        //JoystickButton(controller, 9).whenPressed(ShootCargo())

//        JoystickButton(controller, 1).whenPressed(IntakeCargo)
//        JoystickButton(controller, 2).cancelWhenPressed(IntakeCargo)
//        JoystickButton(controller, 3).whenPressed(SetHatchPanelExtended(true))
//        JoystickButton(controller, 3).whenReleased(SetHatchPanelExtended(false))
//        JoystickButton(controller, 1).cancelWhenPressed(VisionAlign)
    }
}