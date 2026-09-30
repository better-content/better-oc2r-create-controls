package com.bettercontent.betteroc2rcreatecontrols.compat.events

import kotlin.test.Test
import kotlin.test.assertEquals

class SpeedometerEventTrackerTest {
    @Test
    fun `first poll without prime seeds state and emits nothing`() {
        val tracker = SpeedometerEventTracker()
        assertEquals(emptyList(), tracker.poll(10f, false))
        assertEquals(emptyList(), tracker.poll(10f, false))
    }

    @Test
    fun `speed change is deduplicated`() {
        val tracker = SpeedometerEventTracker()
        tracker.prime(32f, false)

        assertEquals(emptyList(), tracker.poll(32f, false))
        assertEquals(
            listOf(CreatePeripheralEvent("speed_change", mapOf("speed" to 16f))),
            tracker.poll(16f, false)
        )
        assertEquals(emptyList(), tracker.poll(16f, false))
    }

    @Test
    fun `overstress collapses speed to zero`() {
        val tracker = SpeedometerEventTracker()
        tracker.prime(64f, false)

        assertEquals(
            listOf(CreatePeripheralEvent("speed_change", mapOf("speed" to 0f))),
            tracker.poll(64f, true)
        )
        assertEquals(emptyList(), tracker.poll(64f, true))
    }
}
