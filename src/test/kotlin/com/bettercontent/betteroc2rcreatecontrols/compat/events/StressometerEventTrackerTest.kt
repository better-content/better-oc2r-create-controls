package com.bettercontent.betteroc2rcreatecontrols.compat.events

import kotlin.test.Test
import kotlin.test.assertEquals

class StressometerEventTrackerTest {
    @Test
    fun `first poll without prime seeds state and emits nothing`() {
        val tracker = StressometerEventTracker()
        assertEquals(emptyList(), tracker.poll(1f, 2f, false))
        assertEquals(emptyList(), tracker.poll(1f, 2f, false))
    }

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
    fun `capacity-only change emits stress change`() {
        val tracker = StressometerEventTracker()
        tracker.prime(12f, 24f, false)

        assertEquals(
            listOf(CreatePeripheralEvent("stress_change", mapOf("stress" to 12f, "capacity" to 30f))),
            tracker.poll(12f, 30f, false)
        )
    }

    @Test
    fun `overstress emits dedicated event and recovery emits stress change`() {
        val tracker = StressometerEventTracker()
        tracker.prime(20f, 30f, false)

        assertEquals(listOf(CreatePeripheralEvent("overstressed")), tracker.poll(20f, 30f, true))
        assertEquals(emptyList(), tracker.poll(25f, 40f, true))
        assertEquals(
            listOf(CreatePeripheralEvent("stress_change", mapOf("stress" to 10f, "capacity" to 30f))),
            tracker.poll(10f, 30f, false)
        )
    }
}
