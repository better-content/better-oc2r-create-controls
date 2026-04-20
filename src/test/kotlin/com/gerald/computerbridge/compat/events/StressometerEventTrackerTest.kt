package com.gerald.computerbridge.compat.events

import kotlin.test.Test
import kotlin.test.assertEquals

class StressometerEventTrackerTest {
    @Test
    fun `stress changes are emitted only when values move`() {
        val tracker = StressometerEventTracker()
        tracker.prime(12f, 24f, false)

        assertEquals(emptyList(), tracker.poll(12f, 24f, false))
        assertEquals(
            listOf(CreatePeripheralEvent("stress_change", mapOf("stress" to 15f, "capacity" to 24f))),
            tracker.poll(15f, 24f, false)
        )
    }

    @Test
    fun `overstress emits dedicated event and recovery emits stress change`() {
        val tracker = StressometerEventTracker()
        tracker.prime(20f, 30f, false)

        assertEquals(listOf(CreatePeripheralEvent("overstressed")), tracker.poll(20f, 30f, true))
        assertEquals(
            listOf(CreatePeripheralEvent("stress_change", mapOf("stress" to 10f, "capacity" to 30f))),
            tracker.poll(10f, 30f, false)
        )
    }
}
