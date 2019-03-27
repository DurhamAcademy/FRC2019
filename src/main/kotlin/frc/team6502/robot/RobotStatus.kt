package frc.team6502.robot

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import frc.team6502.kyberlib.util.units.Length
import frc.team6502.kyberlib.util.units.inches
import frc.team6502.robot.commands.manip.SetElevatorOffset

object RobotStatus {

    var cargoStatus = CargoStatus.NONE
        set(value) {
            field = value
            SmartDashboard.putBoolean("CargoStatusNone", false)
            SmartDashboard.putBoolean("CargoStatusIdle", false)
            SmartDashboard.putBoolean("CargoStatusArmedShip", false)
            SmartDashboard.putBoolean("CargoStatusArmedRocket", false)
        }

    var hatchStatus = HatchStatus.NONE
        set(value) {
            field = value
            SmartDashboard.putBoolean("HatchStatusNone", false)
            SmartDashboard.putBoolean("HatchStatusArmed", false)
        }

    init {
        cargoStatus = CargoStatus.NONE
        hatchStatus = HatchStatus.NONE
    }

    fun setStatusCargo(status: CargoStatus) {
        hatchStatus = HatchStatus.NONE
        cargoStatus = status
        updateElevatorOffset()
    }

    fun setStatusHatch(status: HatchStatus) {
        cargoStatus = CargoStatus.NONE
        hatchStatus = status
        updateElevatorOffset()
    }

    private fun updateElevatorOffset() {
        when {
            cargoStatus != CargoStatus.NONE -> {
                SetElevatorOffset(cargoStatus.heightOffset).start()
            }
            hatchStatus != HatchStatus.NONE -> {
                SetElevatorOffset(hatchStatus.heightOffset).start()
            }
            else -> {
                SetElevatorOffset(0.inches).start()
            }
        }
    }

}

enum class CargoStatus(val heightOffset: Length) {
    NONE(0.inches),
    IDLE(0.inches),
    INTAKING_GROUND(0.inches),
    INTAKING_STATION(28.inches),
    ARMED_SHIP(28.inches),
    ARMED_ROCKET(12.inches)
}

enum class HatchStatus(val heightOffset: Length) {
    NONE(0.inches),
    ARMED(2.5.inches)
}