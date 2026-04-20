package com.gerald.computerbridge.compat.events

import kotlin.test.Test
import kotlin.test.assertEquals

class SpeedometerEventTrackerTest {
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
    }
}
