package com.bettercontent.oc2rcreatebridge.api.devices

import com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics.StressometerAdapter
import com.bettercontent.oc2rcreatebridge.compat.events.StressometerEventTracker
import li.cil.oc2.api.bus.device.`object`.Callback
import net.minecraft.world.level.block.entity.BlockEntity

class StressometerDevice(
    blockEntity: BlockEntity,
    private val adapter: StressometerAdapter,
    private val tracker: StressometerEventTracker = StressometerEventTracker()
) : BaseEventDevice<BlockEntity>(
    blockEntity,
    "Create_Stressometer",
    "create_stressometer"
) {
    @Callback
    fun getStress(): Float = adapter.getStress()

    @Callback
    fun getStressCapacity(): Float = adapter.getStressCapacity()

    override fun primeEventState() {
        tracker.prime(adapter.getStress(), adapter.getStressCapacity(), adapter.isOverStressed())
    }

    override fun pollEvents() = tracker.poll(
        adapter.getStress(),
        adapter.getStressCapacity(),
        adapter.isOverStressed()
    )
}
