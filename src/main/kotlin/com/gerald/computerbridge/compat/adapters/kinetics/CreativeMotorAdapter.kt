package com.gerald.computerbridge.compat.adapters.kinetics

import com.gerald.computerbridge.compat.errors.CreatePeripheralException
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity

class CreativeMotorAdapter(
    private val blockEntity: CreativeMotorBlockEntity
) {
    fun getGeneratedSpeed(): Float = requireNotNull(blockEntity.generatedSpeed) {
        "creative motor speed behaviour is unavailable"
    }.value.toFloat()

    fun setGeneratedSpeed(speed: Int) {
        val generatedSpeed = requireNotNull(blockEntity.generatedSpeed) {
            "creative motor speed behaviour is unavailable"
        }
        if (blockEntity.level == null) {
            throw CreatePeripheralException("creative motor is not attached to a level")
        }
        generatedSpeed.setValue(speed)
    }
}
