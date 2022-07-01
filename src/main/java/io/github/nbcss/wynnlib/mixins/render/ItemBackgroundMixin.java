package io.github.nbcss.wynnlib.mixins.render;

import io.github.nbcss.wynnlib.matcher.ItemMatcher;
import io.github.nbcss.wynnlib.render.RenderKit;
import io.github.nbcss.wynnlib.utils.Color;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class ItemBackgroundMixin extends Screen {
    final Identifier TEXTURE = new Identifier("wynnlib", "textures/slot/circle_2x.png");
    MatrixStack matrixStack = null;

    protected ItemBackgroundMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci){
        matrixStack = matrices;
    }

    @Inject(method = "drawItem", at = @At("HEAD"))
    public void drawItem(ItemStack stack, int x, int y, String amountText, CallbackInfo ci) {
        drawColorSlot(stack, x, y);
    }

    @Redirect(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderInGuiWithOverrides(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;III)V"))
    public void redirect(ItemRenderer instance, LivingEntity entity, ItemStack stack, int x, int y, int seed){
        drawColorSlot(stack, x, y);
        instance.renderInGuiWithOverrides(entity, stack, x, y, seed);
    }

    private void drawColorSlot(ItemStack stack, int x, int y) {
        Color color = ItemMatcher.Companion.toRarityColor(stack);
        if(color != null) {
            matrixStack.push();
            RenderKit.INSTANCE.renderTextureWithColor(matrixStack, TEXTURE, color.toSolidColor(),
                    x - 2, y - 2, 0, 0, 20, 20, 20, 20);
            matrixStack.pop();
        }
    }
}
