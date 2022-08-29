package io.github.nbcss.wynnlib.events

import io.github.nbcss.wynnlib.readers.AbilityTreeHandler
import io.github.nbcss.wynnlib.render.DurabilityRender
import io.github.nbcss.wynnlib.timer.custom.HoundTimerIndicator

object EventRegistry {
    /**
     * Register all event listeners here
     */
    fun registerEvents() {
        InventoryUpdateEvent.registerListener(AbilityTreeHandler)
        //InventoryUpdateEvent.registerListener(XX)
        ArmourStandUpdateEvent.registerListener(HoundTimerIndicator)
        RenderItemOverrideEvent.registerListener(DurabilityRender)
    }
}