package com.bettercontent.betteroc2rcreatecontrols.gametest

import com.bettercontent.betteroc2rcreatecontrols.ComputerBridgeMod
import com.bettercontent.betteroc2rcreatecontrols.compat.events.CreateEventBridge
import com.bettercontent.betteroc2rcreatecontrols.compat.events.TrackedCreateEventSource
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

    @JvmStatic
    @GameTest(template = "blank")
    fun scriptingReportsOnlyCommittedStickerChanges(helper: GameTestHelper) {
        val pos = net.minecraft.core.BlockPos(1, 1, 1)
        helper.setBlock(pos, com.simibubi.create.AllBlocks.STICKER.get())
        val block = helper.getBlockEntity(pos) as com.simibubi.create.content.contraptions.chassis.StickerBlockEntity
        val device = com.bettercontent.betteroc2rcreatecontrols.api.devices.StickerDevice(block,
            com.bettercontent.betteroc2rcreatecontrols.compat.adapters.kinetics.StickerAdapter(block))
        val collector = OperationCollector(block)
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.register(collector)
        try {
            device.isExtended()
            helper.assertTrue(collector.changes == 0, "Reading state cannot report an operation")
            device.extend()
            helper.assertTrue(collector.changes == 1, "The committed extension must report once")
            device.extend()
            helper.assertTrue(collector.changes == 1, "An already extended sticker is a no-op")
            device.retract()
            helper.assertTrue(collector.changes == 2, "A distinct retraction must report its change")
            helper.succeed()
        } finally {
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.unregister(collector)
        }
    }

    class OperationCollector(private val machine: net.minecraft.world.level.block.entity.BlockEntity) {
        var changes = 0
        @net.minecraftforge.eventbus.api.SubscribeEvent
        fun changed(event: com.bettercontent.betteroc2rcreatecontrols.api.events.MachineOperationEvent) {
            if (event.machine === machine) changes++
        }
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
