package com.gerald.computerbridge.api.devices

import com.gerald.computerbridge.compat.adapters.kinetics.StickerAdapter
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
    fun extend(): Boolean = adapter.extend()

    @Callback
    fun retract(): Boolean = adapter.retract()

    @Callback
    fun toggle(): Boolean = adapter.toggle()
}
