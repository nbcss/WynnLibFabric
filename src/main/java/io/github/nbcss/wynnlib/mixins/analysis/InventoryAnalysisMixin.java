package io.github.nbcss.wynnlib.mixins.analysis;

import io.github.nbcss.wynnlib.items.BaseItem;
import io.github.nbcss.wynnlib.matcher.item.ItemMatcher;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class InventoryAnalysisMixin {
    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;" +
            "Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"))
    public void renderTooltip(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo info){
        BaseItem item = ItemMatcher.Companion.toItem(stack);
        if (item != null){
            //todo
        }
    }
}