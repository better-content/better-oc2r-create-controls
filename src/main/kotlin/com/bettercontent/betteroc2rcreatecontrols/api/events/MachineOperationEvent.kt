package com.bettercontent.betteroc2rcreatecontrols.api.events

import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.common.MinecraftForge

/** A scripting callback has committed a changed native property on this apparatus. */
class MachineOperationEvent(val machine: BlockEntity, val operation: String, val before: String, val after: String) : Event() {
    companion object {
        @JvmStatic fun changed(machine: BlockEntity, operation: String, before: Any, after: Any) {
            val changed = before != after
            val log = com.bettercontent.betteroc2rcreatecontrols.diagnostics.PlaytestDiagnostics.LOG
            if (log.enabled("rpc")) {
                val level = machine.level
                if (level != null && !level.isClientSide) log.debug("rpc", "rpc.mutation",
                    com.bettercontent.betteroc2rcreatecontrols.diagnostics.PlaytestLog.Context.server(level.dimension().location().toString(), level.gameTime),
                    if (changed) "observed" else "unchanged", "native_value_returned") {
                    com.bettercontent.betteroc2rcreatecontrols.diagnostics.PlaytestLog.fields("pos_long", machine.blockPos.asLong()) +
                        com.bettercontent.betteroc2rcreatecontrols.diagnostics.FeatureObservation.fields(operation, before, after)
                }
            }
            if (changed && machine.level != null && !machine.level!!.isClientSide) {
                MinecraftForge.EVENT_BUS.post(MachineOperationEvent(machine, operation, before.toString(), after.toString()))
            }
        }
    }
}
