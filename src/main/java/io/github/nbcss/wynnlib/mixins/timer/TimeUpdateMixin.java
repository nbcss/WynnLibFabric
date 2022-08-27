package io.github.nbcss.wynnlib.mixins.timer;

import io.github.nbcss.wynnlib.timer.IndicatorManager;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class TimeUpdateMixin {
    @Inject(method = "setTime", at = @At("HEAD"))
    public void setTime(long time, CallbackInfo ci) {
        IndicatorManager.INSTANCE.update(time);
    }
}
