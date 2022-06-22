package io.github.nbcss.wynnlib.mixins.render;

import net.minecraft.client.render.model.BasicBakedModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BasicBakedModel.class)
public class ItemGuiLightingFixMixin {
    @Inject(method = "isSideLit", at = @At("HEAD"), cancellable = true)
    private void isSideLit(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(false);
    }
}
