package frc.team6502.robot

import edu.wpi.first.wpilibj.GenericHID
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.buttons.JoystickButton
import java.lang.Math.abs
import kotlin.math.pow

object OI {
    val controller = XboxController(0)
    val buttons = arrayOf<Int>().associate { it to JoystickButton(controller, it) }
    val commandingStraight: Boolean
        get() = deadband(controller.x, 0.1) == 0.0

    val commandedY: Double
        get() = deadband(controller.getY(GenericHID.Hand.kLeft), 0.1).pow(3)

    val commandedX: Double
        get() = deadband(controller.getX(GenericHID.Hand.kRight), 0.1).pow(3)

    fun deadband(input: Double, deadband: Double): Double {
        return if (abs(input) < deadband) 0.0 else input
    }

    fun setControllerRumble(rumble: Double) {
        controller.setRumble(GenericHID.RumbleType.kLeftRumble, rumble)
        controller.setRumble(GenericHID.RumbleType.kRightRumble, rumble)
    }
}