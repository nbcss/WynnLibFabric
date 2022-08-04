package io.github.nbcss.wynnlib.mixins.timer;

import io.github.nbcss.wynnlib.timer.ITimer;
import io.github.nbcss.wynnlib.timer.TimerManager;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ResetLevelMixin {
    @Inject(method = "setExperience", at = @At("HEAD"))
    public void setExperience(float progress, int total, int level, CallbackInfo ci) {
        if (progress == 0.0 && total == 0 && level == 0) {
            TimerManager.INSTANCE.onEvent(ITimer.ClearEvent.LEVEL_RESET);
        }
    }
}
