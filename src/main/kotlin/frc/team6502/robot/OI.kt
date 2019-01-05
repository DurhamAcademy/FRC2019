package frc.team6502.robot

import edu.wpi.first.wpilibj.Joystick
import edu.wpi.first.wpilibj.buttons.JoystickButton

object OI {
    val controller = Joystick(0)
    val buttons = arrayOf<Int>().associate { it to JoystickButton(controller, it) }
}