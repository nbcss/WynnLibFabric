package io.github.nbcss.wynnlib.function

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.WynnLibKeybindings
import io.github.nbcss.wynnlib.events.DrawSlotEvent
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.SlotClickEvent
import io.github.nbcss.wynnlib.events.SlotPressEvent
import io.github.nbcss.wynnlib.render.RenderKit
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen
import net.minecraft.client.gui.screen.ingame.HandledScreen
import net.minecraft.client.gui.screen.ingame.HorseScreen
import net.minecraft.client.gui.screen.ingame.InventoryScreen
import net.minecraft.util.Identifier

object SlotLocker {
    private val texture = Identifier("wynnlib", "textures/legacy/lock.png")
    private val client = MinecraftClient.getInstance()
    private fun checkLock(screen: HandledScreen<*>, slot: Int): Boolean {
        if ((client.player?.experienceLevel ?: 0) <= 0)
            return false
        if (screen is InventoryScreen) {
            if (slot in 5..41 && slot != 13 && Settings.isSlotLocked(slot)) {
                return true
            }
        }else if(screen is GenericContainerScreen || screen is HorseScreen) {
            val size = screen.screenHandler.slots.size
            //Those slots should not empty, otherwise not in usual game state
            if (!screen.screenHandler.slots[size - 1].hasStack()) {
                if (!screen.screenHandler.slots[size - 2].hasStack()) {
                    if (!screen.screenHandler.slots[size - 3].hasStack()) {
                        return false
                    }
                }
            }
            val modifiedSlot = 45 + slot - size
            if (modifiedSlot in 9..41 && modifiedSlot != 13 && Settings.isSlotLocked(modifiedSlot)) {
                return true
            }
        }
        return false
    }

    object ClickListener: EventHandler<SlotClickEvent> {
        override fun handle(event: SlotClickEvent) {
            if (checkLock(event.screen, event.slot.id))
                event.cancelled = true
        }
    }

    object PressListener: EventHandler<SlotPressEvent> {
        override fun handle(event: SlotPressEvent) {
            if (WynnLibKeybindings.getLockSlotKeybinding().matchesKey(event.keyCode, event.scanCode)) {
                //modifiedSlot = 45 + event.slot.id - event.screen.screenHandler.slots.size
                val slot = when (event.screen) {
                    is InventoryScreen -> event.slot.id
                    is GenericContainerScreen, is HorseScreen -> 45 + event.slot.id - event.screen.screenHandler.slots.size
                    else -> null
                }
                slot?.let {
                    val locked = Settings.isSlotLocked(it)
                    Settings.setSlotLocked(it, !locked)
                }
            }
            if (client.options.dropKey.matchesKey(event.keyCode, event.scanCode)
                && checkLock(event.screen, event.slot.id))
                event.cancelled = true
        }
    }

    object LockRender: EventHandler<DrawSlotEvent> {
        override fun handle(event: DrawSlotEvent) {
            if (checkLock(event.screen, event.slot.id)) {
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