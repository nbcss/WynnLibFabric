package io.github.nbcss.wynnlib.mixins.world;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientWorld.class)
public class ArmourStandEntityMixin {

    @Inject(method = "addEntity", at = @At("TAIL"))
    public void addEntity(int id, Entity entity, CallbackInfo ci){
        if (entity instanceof ArmorStandEntity) {
            /*System.out.println(id + ": " + entity.getCustomName());
            NbtCompound nbt = new NbtCompound();
            entity.writeNbt(nbt);
            System.out.println(nbt);*/
        }
    }
}
