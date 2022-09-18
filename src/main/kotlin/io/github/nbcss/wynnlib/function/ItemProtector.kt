package io.github.nbcss.wynnlib.function

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.events.DrawSlotEvent
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.InventoryPressEvent
import io.github.nbcss.wynnlib.events.SlotClickEvent
import io.github.nbcss.wynnlib.matcher.ProtectableType
import io.github.nbcss.wynnlib.matcher.item.ItemMatcher
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.playSound
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen
import net.minecraft.client.render.WorldRenderer
import net.minecraft.client.render.entity.EntityRenderer
import net.minecraft.screen.slot.Slot
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import kotlin.math.max

object ItemProtector {
    private val texture = Identifier("wynnlib", "textures/legacy/protect.png")
    private val client = MinecraftClient.getInstance()
    object PressListener: EventHandler<InventoryPressEvent> {
        override fun handle(event: InventoryPressEvent) {
            if (event.screen !is GenericContainerScreen)
                return
            //Loot Chest I
            val title = event.screen.title.asString()
            if (title == "Chest" || title.matches("^Loot Chest (I|II|III|IV)$".toRegex())){
                if (event.keyCode == 256 || client.options.inventoryKey.matchesKey(event.keyCode, event.scanCode)) {
                    val size = max(0, event.screen.screenHandler.slots.size - 36)
                    for (i in (0 until size)) {
                        val slot = event.screen.screenHandler.getSlot(i)
                        if (isSlotProtected(slot)) {
                            playSound(SoundEvents.BLOCK_ANVIL_LAND)
                            event.cancelled = true
                        }
                    }
                }
            }
        }
    }

    //prevent close via pouch slot click
    object ClickListener: EventHandler<SlotClickEvent> {
        override fun handle(event: SlotClickEvent) {
            // no need to do the check if pouch lock already enabled
            if (Settings.getOption(Settings.SettingOption.LOCK_POUCH_IN_CHEST))
                return
            if (event.screen !is GenericContainerScreen)
                return
            val title = event.screen.title.asString()
            if (title == "Chest" || title.matches("^Loot Chest (I|II|III|IV)$".toRegex())){
                if (45 + event.slot.id - event.screen.screenHandler.slots.size == 13) {
                    val size = max(0, event.screen.screenHandler.slots.size - 36)
                    for (i in (0 until size)) {
                        val slot = event.screen.screenHandler.getSlot(i)
                        if (isSlotProtected(slot)) {
                            playSound(SoundEvents.BLOCK_ANVIL_LAND)
                            event.cancelled = true
                        }
                    }
                }
            }
        }
    }

    object ProtectRender: EventHandler<DrawSlotEvent> {
        override fun handle(event: DrawSlotEvent) {
            if (event.screen !is GenericContainerScreen)
                return
            val title = event.screen.title.asString()
            if (title == "Chest" || title.matches("^Loot Chest (I|II|III|IV)$".toRegex())){
                if (event.slot.id < event.screen.screenHandler.slots.size - 36){
                    if (isSlotProtected(event.slot)) {
                        val x = event.slot.x - 2
                        val y = event.slot.y - 2
                        RenderSystem.enableBlend()
                        RenderSystem.disableDepthTest()
                        RenderKit.renderTexture(event.matrices, texture, x, y, 0, 0,
                            20, 20, 20, 20)

                    }
                }
            }
        }
    }

    private fun isSlotProtected(slot: Slot): Boolean {
        if (!slot.hasStack())
            return false
        val matcher = ItemMatcher.toItem(slot.stack)
        if (matcher != null) {
            val type = matcher.getMatcherType()
            if (type is ProtectableType && type.isProtected()) {
                return true
            }
        }
        return false
    }
}