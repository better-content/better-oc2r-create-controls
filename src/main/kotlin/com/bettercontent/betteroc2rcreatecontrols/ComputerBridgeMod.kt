package com.bettercontent.betteroc2rcreatecontrols

import com.bettercontent.betteroc2rcreatecontrols.compat.events.CreateEventBridge
import com.bettercontent.betteroc2rcreatecontrols.integration.oc2r.Oc2rCreateProviders
import com.mojang.logging.LogUtils
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext
import org.slf4j.Logger

@Mod(ComputerBridgeMod.MOD_ID)
class ComputerBridgeMod {
    init {
        Oc2rCreateProviders.BLOCK_DEVICE_PROVIDERS.register(FMLJavaModLoadingContext.get().modEventBus)
        CreateEventBridge.initialize()
        LOGGER.info("Loaded {}", MOD_ID)
    }

    companion object {
        const val MOD_ID: String = "better_oc2r_create_controls"
        val LOGGER: Logger = LogUtils.getLogger()
    }
}
