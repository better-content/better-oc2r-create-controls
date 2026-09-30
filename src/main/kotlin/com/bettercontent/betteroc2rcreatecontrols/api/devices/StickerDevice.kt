package com.bettercontent.betteroc2rcreatecontrols.api.devices

import com.bettercontent.betteroc2rcreatecontrols.compat.adapters.kinetics.StickerAdapter
import li.cil.oc2.api.bus.device.`object`.Callback
import net.minecraft.world.level.block.entity.BlockEntity

class StickerDevice(
    blockEntity: BlockEntity,
    private val adapter: StickerAdapter
) : BaseCreateDevice<BlockEntity>(
    blockEntity,
    "Create_Sticker",
    "create_sticker"
) {
    @Callback
    fun isExtended(): Boolean = adapter.isExtended()

    @Callback
    fun isAttachedToBlock(): Boolean = adapter.isAttachedToBlock()

    @Callback
    fun extend(): Boolean {
        val before = adapter.isExtended()
        val accepted = adapter.extend()
        if (accepted) com.bettercontent.betteroc2rcreatecontrols.api.events.MachineOperationEvent.changed(blockEntity, "extend", before, adapter.isExtended())
        return accepted
    }

    @Callback
    fun retract(): Boolean {
        val before = adapter.isExtended()
        val accepted = adapter.retract()
        if (accepted) com.bettercontent.betteroc2rcreatecontrols.api.events.MachineOperationEvent.changed(blockEntity, "retract", before, adapter.isExtended())
        return accepted
    }

    @Callback
    fun toggle(): Boolean {
        val before = adapter.isExtended()
        val accepted = adapter.toggle()
        if (accepted) com.bettercontent.betteroc2rcreatecontrols.api.events.MachineOperationEvent.changed(blockEntity, "toggle", before, adapter.isExtended())
        return accepted
    }
}
