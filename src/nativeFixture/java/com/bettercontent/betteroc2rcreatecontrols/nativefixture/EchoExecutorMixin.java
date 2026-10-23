package com.bettercontent.betteroc2rcreatecontrols.nativefixture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import java.util.concurrent.Executor;

/** Nonshipping scheduler control: retains and forwards the original task exactly once. */
@Mixin(targets = "li.cil.oc2.common.inet.DefaultSessionLayer", remap = false)
public abstract class EchoExecutorMixin {
    @Redirect(method = "sendSession", at = @At(value = "INVOKE",
            target = "Ljava/util/concurrent/Executor;execute(Ljava/lang/Runnable;)V"), require = 1, expect = 1, allow = 1)
    private void controls$holdOriginalTask(Executor executor, Runnable task) {
        Oc2rCompatibilityTests.dispatch(executor, task);
    }
}
