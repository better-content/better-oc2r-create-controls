package com.gerald.computerbridge.api.devices

import com.gerald.computerbridge.compat.adapters.kinetics.RotationSpeedControllerAdapter
import li.cil.oc2.api.bus.device.`object`.Callback
import li.cil.oc2.api.bus.device.`object`.Parameter
import net.minecraft.world.level.block.entity.BlockEntity

class RotationSpeedControllerDevice(
    blockEntity: BlockEntity,
    private val adapter: RotationSpeedControllerAdapter
) : BaseCreateDevice<BlockEntity>(
    blockEntity,
    "Create_RotationSpeedController",
    "create_rotation_speed_controller"
) {
    @Callback
    fun getTargetSpeed(): Float = adapter.getTargetSpeed()

    @Callback
    fun setTargetSpeed(@Parameter("speed") speed: Int) {
        adapter.setTargetSpeed(speed)
    }
}
