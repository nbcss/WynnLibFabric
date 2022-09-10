package io.github.nbcss.wynnlib.mixins.world;

import io.github.nbcss.wynnlib.events.PlayerSendChatEvent;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class PlayerSendChatMixin {
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        PlayerSendChatEvent event = new PlayerSendChatEvent((ClientPlayerEntity) (Object) this, message);
        PlayerSendChatEvent.Companion.handleEvent(event);
        if (event.getCancelled()){
            ci.cancel();
        }
    }
}
