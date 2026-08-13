package com.bettercontent.oc2rcreatebridge

import com.bettercontent.oc2rcreatebridge.compat.events.CreateEventBridge
import com.bettercontent.oc2rcreatebridge.integration.oc2r.Oc2rCreateProviders
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
        const val MOD_ID: String = "oc2r_create_bridge"
        val LOGGER: Logger = LogUtils.getLogger()
    }
}
