package com.gerald.computerbridge.api.devices

import li.cil.oc2.api.bus.device.`object`.ObjectDevice
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.entity.BlockEntity

abstract class BaseCreateDevice<T : BlockEntity>(
    protected val blockEntity: T,
    vararg val typeNames: String
) {
    fun toObjectDevice(): ObjectDevice = ObjectDevice(this, *typeNames)

    protected fun requireLevel(): Level =
        requireNotNull(blockEntity.level) { "block entity is not attached to a level" }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || other.javaClass != javaClass) return false
        other as BaseCreateDevice<*>
        return blockEntity.blockPos == other.blockEntity.blockPos && levelKey() == other.levelKey()
    }

    override fun hashCode(): Int = 31 * blockEntity.blockPos.hashCode() + levelKey().hashCode()

    private fun levelKey(): ResourceKey<Level>? = blockEntity.level?.dimension()
}
