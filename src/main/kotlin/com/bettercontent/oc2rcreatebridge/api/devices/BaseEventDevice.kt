package com.bettercontent.oc2rcreatebridge.api.devices

import com.bettercontent.oc2rcreatebridge.compat.codecs.Oc2rJsonCodec
import com.bettercontent.oc2rcreatebridge.compat.events.CreateEventBridge
import com.bettercontent.oc2rcreatebridge.compat.events.CreatePeripheralEvent
import com.bettercontent.oc2rcreatebridge.compat.events.TrackedCreateEventSource
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
        }
    }

    override fun unsubscribe(sink: IEventSink) {
        subscribers.remove(sink)
        if (subscribers.isEmpty()) {
            CreateEventBridge.untrack(this)
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
