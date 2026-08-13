package com.bettercontent.oc2rcreatebridge.integration.oc2r

import com.bettercontent.oc2rcreatebridge.ComputerBridgeMod
import com.bettercontent.oc2rcreatebridge.api.devices.CreativeMotorDevice
import com.bettercontent.oc2rcreatebridge.api.devices.RotationSpeedControllerDevice
import com.bettercontent.oc2rcreatebridge.api.devices.SequencedGearshiftDevice
import com.bettercontent.oc2rcreatebridge.api.devices.SpeedometerDevice
import com.bettercontent.oc2rcreatebridge.api.devices.StickerDevice
import com.bettercontent.oc2rcreatebridge.api.devices.StressometerDevice
import com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics.CreativeMotorAdapter
import com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics.RotationSpeedControllerAdapter
import com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics.SequencedGearshiftAdapter
import com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics.SpeedometerAdapter
import com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics.StickerAdapter
import com.bettercontent.oc2rcreatebridge.compat.adapters.kinetics.StressometerAdapter
import com.bettercontent.oc2rcreatebridge.compat.providers.CreateBlockEntityDeviceProvider
import com.simibubi.create.content.contraptions.chassis.StickerBlockEntity
import com.simibubi.create.content.kinetics.gauge.SpeedGaugeBlockEntity
import com.simibubi.create.content.kinetics.gauge.StressGaugeBlockEntity
import com.simibubi.create.content.kinetics.motor.CreativeMotorBlockEntity
import com.simibubi.create.content.kinetics.speedController.SpeedControllerBlockEntity
import com.simibubi.create.content.kinetics.transmission.sequencer.SequencedGearshiftBlockEntity
import li.cil.oc2.api.bus.device.provider.BlockDeviceProvider
import li.cil.oc2.api.util.Registries
import net.minecraftforge.registries.DeferredRegister

object Oc2rCreateProviders {
    val BLOCK_DEVICE_PROVIDERS: DeferredRegister<BlockDeviceProvider> =
        DeferredRegister.create(Registries.BLOCK_DEVICE_PROVIDER, ComputerBridgeMod.MOD_ID)

    init {
        BLOCK_DEVICE_PROVIDERS.register("rotation_speed_controller") {
            CreateBlockEntityDeviceProvider(SpeedControllerBlockEntity::class.java) { blockEntity ->
                RotationSpeedControllerDevice(
                    blockEntity,
                    RotationSpeedControllerAdapter(blockEntity)
                )
            }
        }
        BLOCK_DEVICE_PROVIDERS.register("creative_motor") {
            CreateBlockEntityDeviceProvider(CreativeMotorBlockEntity::class.java) { blockEntity ->
                CreativeMotorDevice(
                    blockEntity,
                    CreativeMotorAdapter(blockEntity)
                )
            }
        }
        BLOCK_DEVICE_PROVIDERS.register("sequenced_gearshift") {
            CreateBlockEntityDeviceProvider(SequencedGearshiftBlockEntity::class.java) { blockEntity ->
                SequencedGearshiftDevice(
                    blockEntity,
                    SequencedGearshiftAdapter(blockEntity)
                )
            }
        }
        BLOCK_DEVICE_PROVIDERS.register("sticker") {
            CreateBlockEntityDeviceProvider(StickerBlockEntity::class.java) { blockEntity ->
                StickerDevice(
                    blockEntity,
                    StickerAdapter(blockEntity)
                )
            }
        }
        BLOCK_DEVICE_PROVIDERS.register("speedometer") {
            CreateBlockEntityDeviceProvider(SpeedGaugeBlockEntity::class.java) { blockEntity ->
                SpeedometerDevice(
                    blockEntity,
                    SpeedometerAdapter(blockEntity)
                )
            }
        }
        BLOCK_DEVICE_PROVIDERS.register("stressometer") {
            CreateBlockEntityDeviceProvider(StressGaugeBlockEntity::class.java) { blockEntity ->
                StressometerDevice(
                    blockEntity,
                    StressometerAdapter(blockEntity)
                )
            }
        }
    }
}
