package io.github.nbcss.wynnlib

import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.MajorId
import io.github.nbcss.wynnlib.gui.dicts.EquipmentDictScreen
import io.github.nbcss.wynnlib.registry.*
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
        //Reload id metadata
        Identification.load()
        MajorId.load()
        //Load database
        PowderRegistry.load()
        RegularEquipmentRegistry.load()
        IngredientRegistry.load()
        AbilityRegistry.load()
        MaterialRegistry.load()
        //Register keybindings
        val openHandbook = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "wynnlib.key.handbook",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "wynnlib.category.keys"
            )
        )
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            while (openHandbook.wasPressed()) {
                it.setScreen(EquipmentDictScreen(it.currentScreen))
            }
        })
    }
}