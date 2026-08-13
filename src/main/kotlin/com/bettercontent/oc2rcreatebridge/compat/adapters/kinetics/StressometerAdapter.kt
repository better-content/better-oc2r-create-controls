package com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics

import com.simibubi.create.content.kinetics.gauge.StressGaugeBlockEntity

class StressometerAdapter(
    private val blockEntity: StressGaugeBlockEntity
) {
    fun getStress(): Float = blockEntity.networkStress

    fun getStressCapacity(): Float = blockEntity.networkCapacity

    fun isOverStressed(): Boolean = blockEntity.isOverStressed
}
