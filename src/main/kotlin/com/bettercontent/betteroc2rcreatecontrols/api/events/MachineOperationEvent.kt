package com.bettercontent.betteroc2rcreatecontrols.api.events

import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.common.MinecraftForge

/** A scripting callback has committed a changed native property on this apparatus. */
class MachineOperationEvent(val machine: BlockEntity, val operation: String, val before: String, val after: String) : Event() {
    companion object {
        @JvmStatic fun changed(machine: BlockEntity, operation: String, before: Any, after: Any) {
            if (before != after && machine.level != null && !machine.level!!.isClientSide) {
                MinecraftForge.EVENT_BUS.post(MachineOperationEvent(machine, operation, before.toString(), after.toString()))
            }
        }
    }
}
