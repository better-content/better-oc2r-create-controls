package com.bettercontent.oc2rcreatebridge.compat.providers

import com.bettercontent.oc2rcreatebridge.api.devices.BaseCreateDevice
import li.cil.oc2.api.bus.device.Device
import li.cil.oc2.api.bus.device.provider.BlockDeviceProvider
import li.cil.oc2.api.bus.device.provider.BlockDeviceQuery
import li.cil.oc2.api.util.Invalidatable
import net.minecraft.world.level.block.entity.BlockEntity

class CreateBlockEntityDeviceProvider<T : BlockEntity>(
    private val blockEntityClass: Class<T>,
    private val factory: (T) -> BaseCreateDevice<*>
) : BlockDeviceProvider {
    override fun getDevice(query: BlockDeviceQuery): Invalidatable<Device> {
        val blockEntity = query.getLevel().getBlockEntity(query.getQueryPosition())
        if (!blockEntityClass.isInstance(blockEntity)) {
            return Invalidatable.empty()
        }

        @Suppress("UNCHECKED_CAST")
        val typedBlockEntity = blockEntity as T
        return Invalidatable.of(factory(typedBlockEntity).toObjectDevice())
    }
}
