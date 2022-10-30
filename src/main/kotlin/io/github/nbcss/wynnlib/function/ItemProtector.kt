package io.github.nbcss.wynnlib.function

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.events.DrawSlotEvent
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.InventoryPressEvent
import io.github.nbcss.wynnlib.events.SlotClickEvent
import io.github.nbcss.wynnlib.items.identity.ConfigurableItem
import io.github.nbcss.wynnlib.items.identity.ItemStarProperty
import io.github.nbcss.wynnlib.items.identity.ProtectableItem
import io.github.nbcss.wynnlib.matcher.ProtectableType
import io.github.nbcss.wynnlib.matcher.item.ItemMatcher
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.playSound
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen
import net.minecraft.screen.slot.Slot
import net.minecraft.sound.SoundEvents
import net.minecraft.util.Identifier
import kotlin.math.max

object ItemProtector {
    private val texture = Identifier("wynnlib", "textures/legacy/protect.png")
    private val client = MinecraftClient.getInstance()
    private val lootTitles: Set<String> = setOf(
        "Chest",
        "Reward Chest",
        "Objective Rewards",
        "Guild Objective Rewards",
        "Daily Rewards",
        "Loot Chest I",
        "Loot Chest II",
        "Loot Chest III",
        "Loot Chest IV",
        "Loot Chest V",     //lol just add an extra tier for fun :d
        "Forgery Chest I",
        "Forgery Chest II",
        "Forgery Chest III",
        "Forgery Chest IV",
        "Forgery Chest V",
        "Forgery Chest VI",
        "Forgery Chest VII",
        "Forgery Chest VIII",
        "Forgery Chest IX",
        "Forgery Chest X"
    )

    fun isLootInventory(title: String): Boolean {
        return title in lootTitles
    }

    object PressListener: EventHandler<InventoryPressEvent> {
        override fun handle(event: InventoryPressEvent) {
            if (event.screen !is GenericContainerScreen)
                return
            val title = event.screen.title.asString()
            if (isLootInventory(title)){
                if (event.keyCode == 256 || client.options.inventoryKey.matchesKey(event.keyCode, event.scanCode)) {
                    val size = max(0, event.screen.screenHandler.slots.size - 36)
                    for (i in (0 until size)) {
                        val slot = event.screen.screenHandler.getSlot(i)
                        if (isSlotProtected(slot)) {
                            playSound(SoundEvents.BLOCK_ANVIL_LAND)
                            event.cancelled = true
                            break
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
            if (isLootInventory(title)){
                if (45 + event.slot.id - event.screen.screenHandler.slots.size == 13) {
                    val size = max(0, event.screen.screenHandler.slots.size - 36)
                    for (i in (0 until size)) {
                        val slot = event.screen.screenHandler.getSlot(i)
                        if (isSlotProtected(slot)) {
                            playSound(SoundEvents.BLOCK_ANVIL_LAND)
                            event.cancelled = true
                            break
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
            if (isLootInventory(title)){
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
        val matchItem = ItemMatcher.toItem(slot.stack)
        if (matchItem != null) {
            val type = matchItem.getMatcherType()
            val baseItem = matchItem.asBaseItem()
            if (type is ProtectableType && type.isProtected()) {
                return true
            }else if(baseItem is ProtectableItem && baseItem.isProtected()){
                return true
            }else if(Settings.getOption(Settings.SettingOption.STARRED_ITEM_PROTECT) && baseItem is ConfigurableItem){
                return ItemStarProperty.hasStar(baseItem)
            }
        }
        return false
    }
}