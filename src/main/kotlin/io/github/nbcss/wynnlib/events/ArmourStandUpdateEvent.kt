package io.github.nbcss.wynnlib.events

import net.minecraft.entity.decoration.ArmorStandEntity

data class ArmourStandUpdateEvent(val entity: ArmorStandEntity) {
    companion object: EventHandler.HandlerList<ArmourStandUpdateEvent>()
}