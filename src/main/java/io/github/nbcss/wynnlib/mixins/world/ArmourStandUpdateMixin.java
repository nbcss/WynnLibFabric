package io.github.nbcss.wynnlib.mixins.world;

import io.github.nbcss.wynnlib.events.ArmourStandUpdateEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.decoration.ArmorStandEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(DataTracker.class)
public class ArmourStandUpdateMixin {
    @Final
    @Shadow
    private Entity trackedEntity;

    @Inject(method = "writeUpdatedEntries", at = @At("TAIL"))
    public void writeUpdatedEntries(List<DataTracker.Entry<?>> entries, CallbackInfo ci) {
        if (trackedEntity instanceof ArmorStandEntity armorStand) {
            ArmourStandUpdateEvent event = new ArmourStandUpdateEvent(armorStand);
            ArmourStandUpdateEvent.Companion.handleEvent(event);
        }
    }
}