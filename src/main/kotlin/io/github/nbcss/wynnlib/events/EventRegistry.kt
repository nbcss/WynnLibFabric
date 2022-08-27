package io.github.nbcss.wynnlib.events

import io.github.nbcss.wynnlib.readers.AbilityTreeHandler
import io.github.nbcss.wynnlib.timer.custom.HoundTimerIndicator

object EventRegistry {
    fun registerEvents() {
        InventoryUpdateEvent.registerListener(AbilityTreeHandler)
        ArmourStandUpdateEvent.registerListener(HoundTimerIndicator)
    }
}