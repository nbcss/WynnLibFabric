package io.github.nbcss.wynnlib

import io.github.nbcss.wynnlib.abilities.IconTexture
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.MajorId
import io.github.nbcss.wynnlib.data.PowderSpecial
import io.github.nbcss.wynnlib.events.EventRegistry
import io.github.nbcss.wynnlib.events.InventoryUpdateEvent
import io.github.nbcss.wynnlib.gui.dicts.EquipmentDictScreen
import io.github.nbcss.wynnlib.readers.AbilityTreeHandler
import io.github.nbcss.wynnlib.registry.*
import io.github.nbcss.wynnlib.timer.status.StatusType
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW


@Suppress("UNUSED")
object WynnLibEntry: ModInitializer {
    private const val MOD_ID = "wynnlib"

    override fun onInitialize() {
        //Reload icons
        IconTexture.reload()
        //Load data
        AbilityRegistry.load()
        Identification.load() //id have to load after ability, because spell id need ability name from abilities...
        StatusType.load()
        MajorId.load()
        PowderSpecial.load()
        PowderRegistry.load()
        RegularEquipmentRegistry.load()
        IngredientRegistry.load()
        MaterialRegistry.load()
        RecipeRegistry.load()
        //Register keybindings
        val openHandbook = registerKey("wynnlib.key.handbook", GLFW.GLFW_KEY_H)
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            while (openHandbook.wasPressed()) {
                it.setScreen(EquipmentDictScreen(it.currentScreen))
            }
        })
        //Register events
        EventRegistry.registerEvents()
    }

    private fun registerKey(name: String, key: Int): KeyBinding {
        return KeyBindingHelper.registerKeyBinding(
            KeyBinding(name, InputUtil.Type.KEYSYM, key, "wynnlib.category.keys")
        )
    }
}