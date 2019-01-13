package frc.team6502.robot

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.buttons.JoystickButton
import java.lang.Math.abs

object OI {
    val controller = Joystick(0)
    val buttons = arrayOf<Int>().associate { it to JoystickButton(controller, it) }
    val commandingStraight: Boolean
    get() = deadband(controller.x, 0.1) == 0.0

    fun deadband(input: Double, deadband: Double): Double {
        return if(abs(input) < deadband) 0.0 else input
    }
}