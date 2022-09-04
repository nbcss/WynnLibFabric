package io.github.nbcss.wynnlib.mixins.render;

import io.github.nbcss.wynnlib.events.InventoryRenderEvent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class InventoryRenderMixin extends Screen {
    @Shadow
    protected int x;
    @Shadow
    protected int y;
    protected InventoryRenderMixin(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("HEAD"))
    public void renderPre(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        InventoryRenderEvent event = new InventoryRenderEvent((HandledScreen<?>) (Object) this,
                matrices, x, y, mouseX, mouseY, delta, InventoryRenderEvent.Phase.PRE);
        InventoryRenderEvent.Companion.handleEvent(event);
    }

    @Inject(method = "render", at = @At("TAIL"))
    public void renderPost(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        InventoryRenderEvent event = new InventoryRenderEvent((HandledScreen<?>) (Object) this,
                matrices, x, y, mouseX, mouseY, delta, InventoryRenderEvent.Phase.POST);
        InventoryRenderEvent.Companion.handleEvent(event);
    }
}
