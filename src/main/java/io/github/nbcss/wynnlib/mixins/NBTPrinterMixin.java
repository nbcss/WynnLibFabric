package io.github.nbcss.wynnlib.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.PrintWriter;
import java.util.List;

@Mixin(HandledScreen.class)
public class NBTPrinterMixin {
    @Shadow
    protected Slot focusedSlot;
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawForeground(Lnet/minecraft/client/util/math/MatrixStack;II)V"))
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        /*if (focusedSlot != null && focusedSlot.hasStack()){
            *//*ItemStack item = focusedSlot.getStack();
            List<Text> tooltip = item.getTooltip(MinecraftClient.getInstance().player, TooltipContext.Default.NORMAL);
            for (Text text : tooltip) {
                System.out.println(text.getString());
            }*//*
        }*/
    }
}
