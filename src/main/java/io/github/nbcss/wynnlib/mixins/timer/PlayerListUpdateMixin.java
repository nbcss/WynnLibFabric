package io.github.nbcss.wynnlib.mixins.timer;

import io.github.nbcss.wynnlib.timer.FooterEntry;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerListHud.class)
public class PlayerListUpdateMixin {
    @Inject(method = "setFooter", at = @At("HEAD"))
    public void setFooter(@Nullable Text footer, CallbackInfo ci) {
        FooterEntry.Companion.updateFooter(footer);
    }
}
