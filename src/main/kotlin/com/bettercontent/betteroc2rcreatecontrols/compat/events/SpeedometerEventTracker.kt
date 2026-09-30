package com.bettercontent.betteroc2rcreatecontrols.compat.events

class SpeedometerEventTracker {
    private var lastVisibleSpeed: Float? = null

    fun prime(speed: Float, overStressed: Boolean) {
        lastVisibleSpeed = visibleSpeed(speed, overStressed)
    }

    fun poll(speed: Float, overStressed: Boolean): List<CreatePeripheralEvent> {
        val visibleSpeed = visibleSpeed(speed, overStressed)
        val previous = lastVisibleSpeed
        lastVisibleSpeed = visibleSpeed

        if (previous == null || previous == visibleSpeed) {
            return emptyList()
        }

        return listOf(
            CreatePeripheralEvent(
                "speed_change",
                mapOf("speed" to visibleSpeed)
            )
        )
    }

    private fun visibleSpeed(speed: Float, overStressed: Boolean): Float = if (overStressed) 0f else speed
}
