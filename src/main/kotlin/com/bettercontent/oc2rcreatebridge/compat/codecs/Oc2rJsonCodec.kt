package com.bettercontent.oc2rcreatebridge.compat.codecs

import com.bettercontent.oc2rcreatebridge.compat.events.CreatePeripheralEvent
import com.google.gson.JsonNull
import com.google.gson.JsonObject

object Oc2rJsonCodec {
    fun toJson(event: CreatePeripheralEvent): JsonObject {
        val json = JsonObject()
        json.addProperty("event", event.name)
        event.payload.forEach { (key, value) ->
            when (value) {
                null -> json.add(key, JsonNull.INSTANCE)
                is Boolean -> json.addProperty(key, value)
                is Number -> json.addProperty(key, value)
                is String -> json.addProperty(key, value)
                else -> json.addProperty(key, value.toString())
            }
        }
        return json
    }
}
