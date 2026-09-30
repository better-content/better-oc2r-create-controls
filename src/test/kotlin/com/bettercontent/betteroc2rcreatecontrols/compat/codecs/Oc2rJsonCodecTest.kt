package com.bettercontent.betteroc2rcreatecontrols.compat.codecs

import com.bettercontent.betteroc2rcreatecontrols.compat.events.CreatePeripheralEvent
import com.google.gson.JsonNull
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class Oc2rJsonCodecTest {
    @Test
    fun `serializes supported payload types without losing event name`() {
        val json = Oc2rJsonCodec.toJson(
            CreatePeripheralEvent(
                "stress_change",
                mapOf(
                    "stress" to 12.5f,
                    "capacity" to 64,
                    "overstressed" to false,
                    "source" to "stressometer",
                    "missing" to null
                )
            )
        )

        assertEquals("stress_change", json["event"].asString)
        assertEquals(12.5f, json["stress"].asFloat)
        assertEquals(64, json["capacity"].asInt)
        assertEquals(false, json["overstressed"].asBoolean)
        assertEquals("stressometer", json["source"].asString)
        assertSame(JsonNull.INSTANCE, json["missing"])
    }

    @Test
    fun `serializes unknown payload values as strings`() {
        val json = Oc2rJsonCodec.toJson(
            CreatePeripheralEvent("custom", mapOf("value" to listOf("a", "b")))
        )

        assertEquals("[a, b]", json["value"].asString)
    }
}
