package io.github.nbcss.wynnlib.events

import net.minecraft.network.MessageType
import net.minecraft.text.Text

class PlayerReceiveChatEvent(val type: MessageType, val message: Text): CancellableEvent() {
    companion object: EventHandler.HandlerList<PlayerReceiveChatEvent>()
}