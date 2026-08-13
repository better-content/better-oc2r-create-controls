package com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics

import com.bettercontent.oc2rcreatebridge.compat.errors.CreatePeripheralException
import com.simibubi.create.AllBlocks
import com.simibubi.create.content.contraptions.chassis.StickerBlock
import com.simibubi.create.content.contraptions.chassis.StickerBlockEntity
import net.minecraft.world.level.block.Block

class StickerAdapter(
    private val blockEntity: StickerBlockEntity
) {
    fun isExtended(): Boolean = blockEntity.isBlockStateExtended

    fun isAttachedToBlock(): Boolean = blockEntity.isBlockStateExtended && blockEntity.isAttachedToBlock

    fun extend(): Boolean = updateExtendedState(true) { !it }

    fun retract(): Boolean = updateExtendedState(false) { it }

    fun toggle(): Boolean {
        val state = blockEntity.blockState
        if (!AllBlocks.STICKER.has(state)) {
            return false
        }
        val level = blockEntity.level ?: throw CreatePeripheralException("sticker is not attached to a level")
        val extended = state.getValue(StickerBlock.EXTENDED)
        level.setBlock(blockEntity.blockPos, state.setValue(StickerBlock.EXTENDED, !extended), Block.UPDATE_CLIENTS)
        return true
    }

    private fun updateExtendedState(target: Boolean, predicate: (Boolean) -> Boolean): Boolean {
        val state = blockEntity.blockState
        if (!AllBlocks.STICKER.has(state)) {
            return false
        }
        val current = state.getValue(StickerBlock.EXTENDED)
        if (!predicate(current)) {
            return false
        }
        val level = blockEntity.level ?: throw CreatePeripheralException("sticker is not attached to a level")
        level.setBlock(blockEntity.blockPos, state.setValue(StickerBlock.EXTENDED, target), Block.UPDATE_CLIENTS)
        return true
    }
}
