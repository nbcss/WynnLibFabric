package io.github.nbcss.wynnlib.mixins.inventory;

import io.github.nbcss.wynnlib.events.InventoryUpdateEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ScreenHandler.class)
public class InventoryUpdateMixin {
    @Inject(method = "updateSlotStacks", at = @At("TAIL"))
    public void onUpdateItems(int revision, List<ItemStack> stacks, ItemStack cursorStack, CallbackInfo ci){
        if (MinecraftClient.getInstance().currentScreen instanceof HandledScreen screen){
            if (screen.getScreenHandler() == (Object) this) {
                Text title = screen.getTitle();
                InventoryUpdateEvent event = new InventoryUpdateEvent(title, stacks, cursorStack);
                InventoryUpdateEvent.Companion.handleEvent(event);
            }
        }
    }
}
