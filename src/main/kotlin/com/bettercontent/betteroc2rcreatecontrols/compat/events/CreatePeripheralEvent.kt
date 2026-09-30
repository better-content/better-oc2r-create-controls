package com.bettercontent.betteroc2rcreatecontrols.compat.events

data class CreatePeripheralEvent(
    val name: String,
    val payload: Map<String, Any?> = emptyMap()
)
