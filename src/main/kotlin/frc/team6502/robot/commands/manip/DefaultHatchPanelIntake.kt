package frc.team6502.robot.commands.manip

import edu.wpi.first.wpilibj.command.Command
import frc.team6502.robot.OI
import frc.team6502.robot.subsystems.HatchPanelIntake

class DefaultHatchPanelIntake : Command() {

    private val distanceThreshold = 200

    init {
        requires(HatchPanelIntake)
    }

    override fun execute() {
        HatchPanelIntake.setCylinders(HatchPanelIntake.ultrasonicDistance < distanceThreshold && !OI.controller.aButton)
    }

    override fun isFinished(): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}