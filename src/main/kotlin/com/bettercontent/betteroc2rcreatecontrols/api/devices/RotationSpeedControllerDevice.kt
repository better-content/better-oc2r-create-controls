package com.bettercontent.betteroc2rcreatecontrols.api.devices

import com.bettercontent.betteroc2rcreatecontrols.compat.adapters.kinetics.RotationSpeedControllerAdapter
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
        val before = adapter.getTargetSpeed()
        adapter.setTargetSpeed(speed)
        com.bettercontent.betteroc2rcreatecontrols.api.events.MachineOperationEvent.changed(blockEntity, "setTargetSpeed", before, adapter.getTargetSpeed())
    }
}
