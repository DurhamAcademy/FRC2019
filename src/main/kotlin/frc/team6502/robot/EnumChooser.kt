package frc.team6502.robot

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard

class EnumChooser(val clazz: Class<*>, val name: String = clazz.name) {
    val chooser = SendableChooser<Int>()

    init {

        val consts = clazz.enumConstants
        for (i in consts.indices) {
            val e = consts[i] as Enum<*>
            if (i == 0) chooser.setDefaultOption(e.name, e.ordinal)
            else chooser.addOption(e.name, e.ordinal)
        }

        SmartDashboard.putData(name, chooser)
        SmartDashboard.setPersistent(name)
    }

    fun value(): Enum<*>? {
        val consts = clazz.enumConstants
        for (i in consts) {
            if ((i as Enum<*>).ordinal == chooser.selected) {
                return i
            }
        }
        return null
    }
}