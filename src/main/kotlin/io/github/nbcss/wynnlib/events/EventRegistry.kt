package io.github.nbcss.wynnlib.events

import io.github.nbcss.wynnlib.function.AnalyzeMode
import io.github.nbcss.wynnlib.function.DurabilityRender
import io.github.nbcss.wynnlib.function.SlotLocker
import io.github.nbcss.wynnlib.readers.AbilityTreeHandler
import io.github.nbcss.wynnlib.render.CharacterInfoInventoryRender
import io.github.nbcss.wynnlib.timer.custom.HoundTimerIndicator

object EventRegistry {
    /**
     * Register all event listeners here
     */
    fun registerEvents() {
        InventoryUpdateEvent.registerListener(AbilityTreeHandler)
        ArmourStandUpdateEvent.registerListener(HoundTimerIndicator)
        RenderItemOverrideEvent.registerListener(DurabilityRender)
        InventoryRenderEvent.registerListener(CharacterInfoInventoryRender)
        SlotClickEvent.registerListener(SlotLocker.ClickListener)
        SlotPressEvent.registerListener(SlotLocker.PressListener)
        DrawSlotEvent.registerListener(SlotLocker.LockRender)
        ItemLoadEvent.registerListener(DurabilityRender.LoadListener)
        ItemLoadEvent.registerListener(AnalyzeMode)
    }
}