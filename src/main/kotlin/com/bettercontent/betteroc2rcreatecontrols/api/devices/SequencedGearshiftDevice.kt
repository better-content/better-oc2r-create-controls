package com.bettercontent.betteroc2rcreatecontrols.api.devices

import com.bettercontent.betteroc2rcreatecontrols.compat.adapters.kinetics.SequencedGearshiftAdapter
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
        val wasRunning = adapter.isRunning()
        adapter.rotate(angle, modifier ?: 1)
        com.bettercontent.betteroc2rcreatecontrols.api.events.MachineOperationEvent.changed(blockEntity, "rotate", wasRunning, adapter.isRunning())
    }

    @Callback
    fun move(
        @Parameter("distance") distance: Int,
        @Parameter("modifier") modifier: Int? = null
    ) {
        val wasRunning = adapter.isRunning()
        adapter.move(distance, modifier ?: 1)
        com.bettercontent.betteroc2rcreatecontrols.api.events.MachineOperationEvent.changed(blockEntity, "move", wasRunning, adapter.isRunning())
    }

    @Callback
    fun isRunning(): Boolean = adapter.isRunning()
}
