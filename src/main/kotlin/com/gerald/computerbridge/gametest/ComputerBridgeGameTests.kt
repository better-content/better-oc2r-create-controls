package com.gerald.computerbridge.gametest

import com.gerald.computerbridge.ComputerBridgeMod
import com.gerald.computerbridge.compat.events.CreateEventBridge
import com.gerald.computerbridge.compat.events.TrackedCreateEventSource
import net.minecraft.gametest.framework.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraftforge.gametest.GameTestHolder

@GameTestHolder(ComputerBridgeMod.MOD_ID)
object ComputerBridgeGameTests {
    @JvmStatic
    @GameTest(template = "blank")
    fun eventBridgePrunesInvalidSources(helper: GameTestHelper) {
        CreateEventBridge.clearTrackedSourcesForTests()
        val invalid = GameTestTrackedSource(valid = false)
        val active = GameTestTrackedSource()

        CreateEventBridge.track(invalid)
        CreateEventBridge.track(active)
        CreateEventBridge.tickTrackedSources()
        CreateEventBridge.tickTrackedSources()
        CreateEventBridge.clearTrackedSourcesForTests()

        helper.assertTrue(invalid.ticks == 0, "Expected invalid source to be pruned before ticking")
        helper.assertTrue(active.ticks == 2, "Expected valid subscribed source to tick twice")
        helper.succeed()
    }

    private class GameTestTrackedSource(
        private val subscribed: Boolean = true,
        private val valid: Boolean = true
    ) : TrackedCreateEventSource {
        var ticks = 0

        override fun hasSubscribers(): Boolean = subscribed

        override fun isValidSource(): Boolean = valid

        override fun bridgeTick() {
            ticks += 1
        }
    }
}
