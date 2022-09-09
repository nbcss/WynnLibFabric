package io.github.nbcss.wynnlib.mixins.render;

import io.github.nbcss.wynnlib.events.DrawSlotEvent;
import io.github.nbcss.wynnlib.events.RenderItemOverrideEvent;
import io.github.nbcss.wynnlib.matcher.color.ColorMatcher;
import io.github.nbcss.wynnlib.render.RenderKit;
import io.github.nbcss.wynnlib.utils.Color;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class ItemBackgroundMixin extends Screen {
    final Identifier TEXTURE = new Identifier("wynnlib", "textures/slot/circle.png");
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

    @Inject(method = "drawSlot", at = @At("HEAD"))
    private void drawSlot(MatrixStack matrices, Slot slot, CallbackInfo ci) {
        DrawSlotEvent event = new DrawSlotEvent((HandledScreen<?>) (Object) this, matrices, slot);
        DrawSlotEvent.Companion.handleEvent(event);
    }

    @Redirect(method = "drawItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"))
    public void drawItemInvoke(ItemRenderer instance, TextRenderer renderer, ItemStack stack, int x, int y, String countLabel) {
        if (drawOverrides(renderer, stack, x, y))
            return;
        instance.renderGuiItemOverlay(renderer, stack, x, y, countLabel);
    }

    @Redirect(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderInGuiWithOverrides(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;III)V"))
    public void redirect(ItemRenderer instance, LivingEntity entity, ItemStack stack, int x, int y, int seed){
        drawColorSlot(stack, x, y);
        instance.renderInGuiWithOverrides(entity, stack, x, y, seed);
    }

    @Redirect(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V"))
    public void redirect(ItemRenderer instance, TextRenderer renderer, ItemStack stack, int x, int y, String countLabel){
        if (drawOverrides(renderer, stack, x, y))
            return;
        instance.renderGuiItemOverlay(renderer, stack, x, y, countLabel);
    }

    private boolean drawOverrides(TextRenderer renderer, ItemStack stack, int x, int y) {
        RenderItemOverrideEvent event = new RenderItemOverrideEvent(renderer, stack, x, y);
        RenderItemOverrideEvent.Companion.handleEvent(event);
        return event.getCancelled();
    }

    private void drawColorSlot(ItemStack stack, int x, int y) {
        Color color = ColorMatcher.Companion.toRarityColor(stack);
        if(color != null) {
            matrixStack.push();
            matrixStack.translate(0.0, 0.0, 200.0);
            RenderKit.INSTANCE.renderTextureWithColor(matrixStack, TEXTURE, color.solid(),
                    x - 2, y - 2, 0, 0, 20, 20, 20, 20);
            matrixStack.pop();
        }
    }
}
