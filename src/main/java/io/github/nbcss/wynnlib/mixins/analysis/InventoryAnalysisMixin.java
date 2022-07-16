package io.github.nbcss.wynnlib.mixins.analysis;

import io.github.nbcss.wynnlib.Settings;
import io.github.nbcss.wynnlib.items.BaseItem;
import io.github.nbcss.wynnlib.matcher.item.ItemMatcher;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(Screen.class)
public abstract class InventoryAnalysisMixin {
    @Inject(method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;" +
            "Lnet/minecraft/item/ItemStack;II)V", at = @At("HEAD"), cancellable = true)
    public void renderTooltip(MatrixStack matrices, ItemStack stack, int x, int y, CallbackInfo info){
        if(Settings.INSTANCE.isAnalysisModeEnabled()){
            BaseItem item = ItemMatcher.Companion.toItem(stack);
            if (item != null){
                //todo create analysis tooltip
                List<Text> tooltip = item.getTooltip();
                //renderTooltip(matrices, tooltip, stack.getTooltipData(), x, y);
                info.cancel();
            }
        }
    }

    @Shadow
    public abstract void renderTooltip(MatrixStack matrices, List<Text> lines, Optional<TooltipData> data, int x, int y);
}