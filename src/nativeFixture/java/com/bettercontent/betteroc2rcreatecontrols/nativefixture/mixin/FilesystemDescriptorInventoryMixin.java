package com.bettercontent.betteroc2rcreatecontrols.nativefixture.mixin;

import com.bettercontent.betteroc2rcreatecontrols.nativefixture.FilesystemDescriptorInventory;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import li.cil.oc2.common.bus.device.data.FileSystems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import java.util.Map;
import java.util.function.Predicate;

@Mixin(value = FileSystems.class, remap = false)
public abstract class FilesystemDescriptorInventoryMixin {
    @WrapOperation(method = "reload(Lnet/minecraft/server/packs/resources/ResourceManager;)V", at = {
            @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ResourceManager;listResources(Ljava/lang/String;Ljava/util/function/Predicate;)Ljava/util/Map;", remap = false),
            @At(value = "INVOKE", target = "Lnet/minecraft/server/packs/resources/ResourceManager;m_214159_(Ljava/lang/String;Ljava/util/function/Predicate;)Ljava/util/Map;", remap = false)
    }, remap = false, require = 1, expect = 1, allow = 1)
    private static Map<ResourceLocation, Resource> betterControls$captureDescriptorIds(ResourceManager manager,
            String prefix, Predicate<ResourceLocation> filter, Operation<Map<ResourceLocation, Resource>> original) {
        Map<ResourceLocation, Resource> result = original.call(manager, prefix, filter);
        // Original is outside this diagnostic-only catch: preserve exact Throwable and return identity.
        try {
            FilesystemDescriptorInventory.capture(result);
        } catch (Throwable diagnosticFailure) {
            // Capture remains absent/failed; the owning test fails normally, never this native operation.
        }
        return result;
    }
}
