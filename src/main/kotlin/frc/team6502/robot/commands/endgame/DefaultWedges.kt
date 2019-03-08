package frc.team6502.robot.commands.endgame

import edu.wpi.first.wpilibj.Timer
import edu.wpi.first.wpilibj.command.Command
import frc.team6502.robot.OI
import frc.team6502.robot.commandgroups.DeployWedges
import frc.team6502.robot.subsystems.Wedges

class DefaultWedges : Command() {
    private val t = Timer()

    init {
        requires(Wedges)
    }

    override fun initialize() {
        t.reset()
        t.stop()
    }

    override fun isFinished() = false

    override fun execute() {
        if (OI.controller.getRawButton(7)) {
            if (t.get() == 0.0) {
                t.start()
            }
        } else {
            t.stop()
            t.reset()
        }
        if (t.get() > 1.0 && !Wedges.deployed) {
            DeployWedges().start()
        }
    }
}