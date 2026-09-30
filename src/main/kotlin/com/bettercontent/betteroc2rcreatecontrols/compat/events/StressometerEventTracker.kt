package com.bettercontent.betteroc2rcreatecontrols.compat.events

class StressometerEventTracker {
    private var lastStress: Float? = null
    private var lastCapacity: Float? = null
    private var lastOverStressed: Boolean? = null

    fun prime(stress: Float, capacity: Float, overStressed: Boolean) {
        lastStress = stress
        lastCapacity = capacity
        lastOverStressed = overStressed
    }

    fun poll(stress: Float, capacity: Float, overStressed: Boolean): List<CreatePeripheralEvent> {
        val previousOverStressed = lastOverStressed
        val previousStress = lastStress
        val previousCapacity = lastCapacity

        lastStress = stress
        lastCapacity = capacity
        lastOverStressed = overStressed

        if (previousOverStressed == null) {
            return emptyList()
        }

        val events = mutableListOf<CreatePeripheralEvent>()
        if (!previousOverStressed && overStressed) {
            events += CreatePeripheralEvent("overstressed")
        }
        if (!overStressed && (previousOverStressed || previousStress != stress || previousCapacity != capacity)) {
            events += CreatePeripheralEvent(
                "stress_change",
                mapOf(
                    "stress" to stress,
                    "capacity" to capacity
                )
            )
        }
        return events
    }
}
