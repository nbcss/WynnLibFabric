package io.github.nbcss.wynnlib.mixins.inventory;

import com.mojang.authlib.GameProfile;
import io.github.nbcss.wynnlib.Settings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerDropItemMixin extends PlayerEntity {

    public PlayerDropItemMixin(World world, BlockPos pos, float yaw, GameProfile profile) {
        super(world, pos, yaw, profile);
    }

    @Inject(method = "dropSelectedItem", at = @At("HEAD"), cancellable = true)
    public void dropSelectedItem(boolean entireStack, CallbackInfoReturnable<Boolean> cir) {
        int slot = getInventory().selectedSlot + 36;
        if (slot < 42 && Settings.INSTANCE.isSlotLocked(slot)) {
            cir.setReturnValue(false);
        }
    }
}
