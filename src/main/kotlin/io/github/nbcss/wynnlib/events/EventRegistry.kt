package io.github.nbcss.wynnlib.events

import io.github.nbcss.wynnlib.inventory.SlotLocker
import io.github.nbcss.wynnlib.readers.AbilityTreeHandler
import io.github.nbcss.wynnlib.render.CharacterInfoInventoryRender
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
        InventoryRenderEvent.registerListener(CharacterInfoInventoryRender)
        SlotClickEvent.registerListener(SlotLocker.ClickListener)
        SlotPressEvent.registerListener(SlotLocker.PressListener)
        DrawSlotEvent.registerListener(SlotLocker.LockRender)
    }
}