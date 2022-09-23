package io.github.nbcss.wynnlib.function

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.SlotClickEvent
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen

object PouchInChest {

    object ClickListener: EventHandler<SlotClickEvent> {
        override fun handle(event: SlotClickEvent) {
            if (!Settings.getOption(Settings.SettingOption.LOCK_POUCH_IN_CHEST))
                return
            if (event.screen !is GenericContainerScreen)
                return
            val title = event.screen.title.asString()
            if (ItemProtector.isLootInventory(title)){
                val size = event.screen.screenHandler.slots.size
                if (45 + event.slot.id - size == 13) {
                    event.cancelled = true
                }
            }
        }
    }
}