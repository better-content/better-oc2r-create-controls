package com.bettercontent.oc2rcreatebridge.api.devices

import com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics.SpeedometerAdapter
import com.bettercontent.oc2rcreatebridge.compat.events.SpeedometerEventTracker
import li.cil.oc2.api.bus.device.`object`.Callback
import net.minecraft.world.level.block.entity.BlockEntity

class SpeedometerDevice(
    blockEntity: BlockEntity,
    private val adapter: SpeedometerAdapter,
    private val tracker: SpeedometerEventTracker = SpeedometerEventTracker()
) : BaseEventDevice<BlockEntity>(
    blockEntity,
    "Create_Speedometer",
    "create_speedometer"
) {
    @Callback
    fun getSpeed(): Float = adapter.getSpeed()

    override fun primeEventState() {
        tracker.prime(adapter.getSpeed(), adapter.isOverStressed())
    }

    override fun pollEvents() = tracker.poll(adapter.getSpeed(), adapter.isOverStressed())
}
