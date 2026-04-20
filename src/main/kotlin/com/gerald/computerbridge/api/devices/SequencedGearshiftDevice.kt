package com.gerald.computerbridge.api.devices

import com.gerald.computerbridge.compat.adapters.kinetics.SequencedGearshiftAdapter
import li.cil.oc2.api.bus.device.`object`.Callback
import li.cil.oc2.api.bus.device.`object`.Parameter
import net.minecraft.world.level.block.entity.BlockEntity

class SequencedGearshiftDevice(
    blockEntity: BlockEntity,
    private val adapter: SequencedGearshiftAdapter
) : BaseCreateDevice<BlockEntity>(
    blockEntity,
    "Create_SequencedGearshift",
    "create_sequenced_gearshift"
) {
    @Callback
    fun rotate(
        @Parameter("angle") angle: Int,
        @Parameter("modifier") modifier: Int? = null
    ) {
        adapter.rotate(angle, modifier ?: 1)
    }

    @Callback
    fun move(
        @Parameter("distance") distance: Int,
        @Parameter("modifier") modifier: Int? = null
    ) {
        adapter.move(distance, modifier ?: 1)
    }

    @Callback
    fun isRunning(): Boolean = adapter.isRunning()
}
