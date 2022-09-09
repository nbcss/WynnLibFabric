package io.github.nbcss.wynnlib.events

import net.minecraft.network.Packet

class PacketSendingEvent(val packet: Packet<*>): CancellableEvent() {
    companion object: EventHandler.HandlerList<PacketSendingEvent>()
}