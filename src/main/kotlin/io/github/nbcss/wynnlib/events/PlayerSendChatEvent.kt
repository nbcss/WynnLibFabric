package io.github.nbcss.wynnlib.events

import net.minecraft.client.network.ClientPlayerEntity

class PlayerSendChatEvent(val player: ClientPlayerEntity,
                          val message: String): CancellableEvent() {
    companion object: EventHandler.HandlerList<PlayerSendChatEvent>()
}