package com.gerald.computerbridge.compat.events

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.TickEvent
import net.minecraftforge.event.server.ServerStoppedEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

object CreateEventBridge {
    private val trackedSources = linkedSetOf<TrackedCreateEventSource>()
    private var initialized = false

    fun initialize() {
        if (initialized) {
            return
        }
        initialized = true
        MinecraftForge.EVENT_BUS.register(this)
    }

    fun track(source: TrackedCreateEventSource) {
        trackedSources += source
    }

    fun untrack(source: TrackedCreateEventSource) {
        trackedSources -= source
    }

    @SubscribeEvent
    fun handleServerTick(event: TickEvent.ServerTickEvent) {
        if (event.phase != TickEvent.Phase.END) {
            return
        }

        val iterator = trackedSources.iterator()
        while (iterator.hasNext()) {
            val source = iterator.next()
            if (!source.hasSubscribers() || !source.isValidSource()) {
                iterator.remove()
                continue
            }
            source.bridgeTick()
        }
    }

    @SubscribeEvent
    fun handleServerStopped(@Suppress("UNUSED_PARAMETER") event: ServerStoppedEvent) {
        trackedSources.clear()
    }
}
