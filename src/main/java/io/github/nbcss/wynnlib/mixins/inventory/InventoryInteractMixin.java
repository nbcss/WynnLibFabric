package io.github.nbcss.wynnlib.mixins.inventory;

import io.github.nbcss.wynnlib.events.InventoryPressEvent;
import io.github.nbcss.wynnlib.events.SlotClickEvent;
import io.github.nbcss.wynnlib.events.SlotPressEvent;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.lwjgl.system.CallbackI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class InventoryInteractMixin {
    @Shadow
    protected Slot focusedSlot;
    @Shadow
    protected abstract Slot getSlotAt(double x, double y);


    @Inject(method = "mouseReleased", at = @At("HEAD"), cancellable = true)
    public void mouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        Slot slot = this.getSlotAt(mouseX, mouseY);
        if (slot != null) {
            SlotClickEvent event = new SlotClickEvent((HandledScreen<?>) (Object) this, slot, button);
            SlotClickEvent.Companion.handleEvent(event);
            if (event.getCancelled()) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "mouseClicked", at = @At("HEAD"), cancellable = true)
    public void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        Slot slot = this.getSlotAt(mouseX, mouseY);
        if (slot != null) {
            SlotClickEvent event = new SlotClickEvent((HandledScreen<?>) (Object) this, slot, button);
            SlotClickEvent.Companion.handleEvent(event);
            if (event.getCancelled()) {
                cir.setReturnValue(true);
            }
        }
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    public void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        HandledScreen<?> screen = (HandledScreen<?>) (Object) this;
        {
            InventoryPressEvent event = new InventoryPressEvent(screen, keyCode, scanCode);
            InventoryPressEvent.Companion.handleEvent(event);
            if (event.getCancelled()) {
                cir.setReturnValue(true);
                return;
            }
        }
        if (focusedSlot != null) {
            SlotPressEvent event = new SlotPressEvent(screen, focusedSlot, keyCode, scanCode);
            SlotPressEvent.Companion.handleEvent(event);
            if (event.getCancelled()) {
                cir.setReturnValue(true);
            }
        }
    }
}
