package com.bettercontent.oc2rcreatebridge.api.devices

import com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics.CreativeMotorAdapter
import li.cil.oc2.api.bus.device.`object`.Callback
import li.cil.oc2.api.bus.device.`object`.Parameter
import net.minecraft.world.level.block.entity.BlockEntity

class CreativeMotorDevice(
    blockEntity: BlockEntity,
    private val adapter: CreativeMotorAdapter
) : BaseCreateDevice<BlockEntity>(
    blockEntity,
    "Create_CreativeMotor",
    "create_creative_motor"
) {
    @Callback
    fun getGeneratedSpeed(): Float = adapter.getGeneratedSpeed()

    @Callback
    fun setGeneratedSpeed(@Parameter("speed") speed: Int) {
        val before = adapter.getGeneratedSpeed()
        adapter.setGeneratedSpeed(speed)
        com.bettercontent.oc2rcreatebridge.api.events.MachineOperationEvent.changed(blockEntity, "setGeneratedSpeed", before, adapter.getGeneratedSpeed())
    }
}
