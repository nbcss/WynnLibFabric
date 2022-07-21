package io.github.nbcss.wynnlib.mixins.analysis;

import io.github.nbcss.wynnlib.utils.keys.AnalysisToggle;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class ToggleAnalysisMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    public void tick(CallbackInfo ci){
        AnalysisToggle.INSTANCE.tick();
    }
}
