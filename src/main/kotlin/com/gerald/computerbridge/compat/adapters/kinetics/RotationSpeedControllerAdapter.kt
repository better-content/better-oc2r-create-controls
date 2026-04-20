package com.gerald.computerbridge.compat.adapters.kinetics

import com.gerald.computerbridge.compat.errors.CreatePeripheralException
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlockEntity

class RotationSpeedControllerAdapter(
    private val blockEntity: SpeedControllerBlockEntity
) {
    fun getTargetSpeed(): Float = requireNotNull(blockEntity.targetSpeed) {
        "speed controller target speed behaviour is unavailable"
    }.value.toFloat()

    fun setTargetSpeed(speed: Int) {
        val targetSpeed = requireNotNull(blockEntity.targetSpeed) {
            "speed controller target speed behaviour is unavailable"
        }
        if (blockEntity.level == null) {
            throw CreatePeripheralException("speed controller is not attached to a level")
        }
        targetSpeed.setValue(speed)
    }
}
