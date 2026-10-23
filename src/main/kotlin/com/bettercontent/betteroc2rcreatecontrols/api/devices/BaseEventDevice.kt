package com.bettercontent.betteroc2rcreatecontrols.api.devices

import com.bettercontent.betteroc2rcreatecontrols.compat.codecs.Oc2rJsonCodec
import com.bettercontent.betteroc2rcreatecontrols.compat.events.CreateEventBridge
import com.bettercontent.betteroc2rcreatecontrols.compat.events.CreatePeripheralEvent
import com.bettercontent.betteroc2rcreatecontrols.compat.events.TrackedCreateEventSource
import li.cil.oc2.api.bus.device.rpc.IEventSink
import li.cil.oc2.api.bus.device.rpc.RPCEventSource
import net.minecraft.world.level.block.entity.BlockEntity
import java.util.UUID

abstract class BaseEventDevice<T : BlockEntity>(
    blockEntity: T,
    vararg typeNames: String
) : BaseCreateDevice<T>(blockEntity, *typeNames), RPCEventSource, TrackedCreateEventSource {
    private val subscribers = linkedMapOf<IEventSink, UUID>()

    override fun subscribe(sink: IEventSink, sourceId: UUID) {
        val firstSubscriber = subscribers.isEmpty()
        subscribers[sink] = sourceId
        if (firstSubscriber) {
            primeEventState()
            CreateEventBridge.track(this)
            observeSubscription("first_subscriber")
        }
    }

    override fun unsubscribe(sink: IEventSink) {
        val removed = subscribers.remove(sink)
        if (subscribers.isEmpty()) {
            CreateEventBridge.untrack(this)
            if (removed != null) observeSubscription("last_subscriber_removed")
        }
    }

    private fun observeSubscription(reason: String) {
        val log = com.bettercontent.betteroc2rcreatecontrols.diagnostics.PlaytestDiagnostics.LOG
        if (!log.enabled("subscriptions")) return
        val level = blockEntity.level ?: return
        if (level.isClientSide) return
        log.debug("subscriptions", "rpc.subscription", com.bettercontent.betteroc2rcreatecontrols.diagnostics.PlaytestLog.Context.server(
            level.dimension().location().toString(), level.gameTime), "observed", reason) {
            com.bettercontent.betteroc2rcreatecontrols.diagnostics.PlaytestLog.fields("pos_long", blockEntity.blockPos.asLong(), "subscribers", subscribers.size)
        }
    }

    override fun hasSubscribers(): Boolean = subscribers.isNotEmpty()

    override fun isValidSource(): Boolean = blockEntity.level != null && !blockEntity.isRemoved

    override fun bridgeTick() {
        pollEvents().forEach(::dispatch)
    }

    protected abstract fun primeEventState()

    protected abstract fun pollEvents(): List<CreatePeripheralEvent>

    private fun dispatch(event: CreatePeripheralEvent) {
        if (subscribers.isEmpty()) {
            return
        }

        val payload = Oc2rJsonCodec.toJson(event)
        subscribers.forEach { (sink, sourceId) ->
            sink.postEvent(sourceId, payload)
        }
    }
}
