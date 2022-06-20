package io.github.nbcss.wynnlib.mixins;

import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemLightingFixMixin {
    @Inject(method = "renderGuiItemModel", at = @At("HEAD"))
    protected void renderGuiItemModel(ItemStack stack, int x, int y, BakedModel model, CallbackInfo ci) {
        //DiffuseLighting.enableGuiDepthLighting();
        //todo fix lighting ._.
    }
}
