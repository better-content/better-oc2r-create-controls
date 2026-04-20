package com.gerald.computerbridge.compat.adapters.kinetics

import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity

class SpeedometerAdapter(
    private val blockEntity: SpeedGaugeBlockEntity
) {
    fun getSpeed(): Float = blockEntity.speed

    fun isOverStressed(): Boolean = blockEntity.isOverStressed
}
