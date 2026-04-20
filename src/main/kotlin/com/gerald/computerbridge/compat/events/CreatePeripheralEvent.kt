package com.gerald.computerbridge.compat.events

data class CreatePeripheralEvent(
    val name: String,
    val payload: Map<String, Any?> = emptyMap()
)
