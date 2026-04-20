package com.gerald.computerbridge.api.devices

import com.gerald.computerbridge.compat.adapters.kinetics.CreativeMotorAdapter
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
        adapter.setGeneratedSpeed(speed)
    }
}
