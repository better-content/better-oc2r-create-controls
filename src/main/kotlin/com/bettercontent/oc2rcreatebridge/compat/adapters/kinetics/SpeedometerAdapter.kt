package com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics

import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity

class SpeedometerAdapter(
    private val blockEntity: SpeedGaugeBlockEntity
) {
    fun getSpeed(): Float = blockEntity.speed

    fun isOverStressed(): Boolean = blockEntity.isOverStressed
}
