package com.bettercontent.oc2rcreatebridge.compat.events

import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

class CreateEventBridgeTest {
    @AfterTest
    fun tearDown() {
        CreateEventBridge.clearTrackedSourcesForTests()
    }

    @Test
    fun `ticks subscribed valid sources`() {
        val source = FakeTrackedSource()

        CreateEventBridge.track(source)
        CreateEventBridge.tickTrackedSources()
        CreateEventBridge.tickTrackedSources()

        assertEquals(2, source.ticks)
    }

    @Test
    fun `prunes unsubscribed or invalid sources before ticking`() {
        val unsubscribed = FakeTrackedSource(subscribed = false)
        val invalid = FakeTrackedSource(valid = false)
        val active = FakeTrackedSource()

        CreateEventBridge.track(unsubscribed)
        CreateEventBridge.track(invalid)
        CreateEventBridge.track(active)

        CreateEventBridge.tickTrackedSources()
        CreateEventBridge.tickTrackedSources()

        assertEquals(0, unsubscribed.ticks)
        assertEquals(0, invalid.ticks)
        assertEquals(2, active.ticks)
    }

    private class FakeTrackedSource(
        private val subscribed: Boolean = true,
        private val valid: Boolean = true
    ) : TrackedCreateEventSource {
        var ticks = 0

        override fun hasSubscribers(): Boolean = subscribed

        override fun isValidSource(): Boolean = valid

        override fun bridgeTick() {
            ticks += 1
        }
    }
}
