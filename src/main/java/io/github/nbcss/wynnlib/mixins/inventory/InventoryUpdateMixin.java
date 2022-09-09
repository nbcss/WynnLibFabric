package io.github.nbcss.wynnlib.mixins.inventory;

import io.github.nbcss.wynnlib.events.InventoryUpdateEvent;
import io.github.nbcss.wynnlib.events.ItemLoadEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
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

    @ModifyVariable(method = "updateSlotStacks", at = @At("HEAD"), index = 3, argsOnly = true)
    public ItemStack onModifyCursorItem(ItemStack stack) {
        if (stack.isEmpty())
            return stack;
        ItemLoadEvent event = new ItemLoadEvent(stack);
        ItemLoadEvent.Companion.handleEvent(event);
        return event.getItem();
    }

    @ModifyVariable(method = "updateSlotStacks", at = @At("HEAD"), index = 2, argsOnly = true)
    public List<ItemStack> onModifyItems(List<ItemStack> stacks){
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            if (stack.isEmpty())
                continue;
            ItemLoadEvent event = new ItemLoadEvent(stack);
            ItemLoadEvent.Companion.handleEvent(event);
            stacks.set(i, event.getItem());
        }
        return stacks;
    }

    @ModifyVariable(method = "setStackInSlot", at = @At("HEAD"), index = 3, argsOnly = true)
    public ItemStack onModifyItem(ItemStack stack) {
        if (stack.isEmpty())
            return stack;
        ItemLoadEvent event = new ItemLoadEvent(stack);
        ItemLoadEvent.Companion.handleEvent(event);
        return event.getItem();
    }

    @ModifyVariable(method = "setCursorStack", at = @At("HEAD"), index = 1, argsOnly = true)
    public ItemStack setCursorStack(ItemStack stack) {
        if (stack.isEmpty())
            return stack;
        ItemLoadEvent event = new ItemLoadEvent(stack);
        ItemLoadEvent.Companion.handleEvent(event);
        return event.getItem();
    }
}
