package com.bettercontent.oc2rcreatebridge.compat.events

data class CreatePeripheralEvent(
    val name: String,
    val payload: Map<String, Any?> = emptyMap()
)
