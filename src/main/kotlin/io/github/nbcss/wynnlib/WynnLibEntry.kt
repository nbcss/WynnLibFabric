package io.github.nbcss.wynnlib

import io.github.nbcss.wynnlib.gui.EquipmentDictScreen
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import net.minecraft.text.LiteralText
import org.lwjgl.glfw.GLFW


@Suppress("UNUSED")
object WynnLibEntry: ModInitializer {
    private const val MOD_ID = "wynnlib"

    override fun onInitialize() {
        //println("Example mod has been initialized.")
        val openHandbook = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                "key.wynnlib.handbook",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_H,
                "category.wynnlib.keys"
            )
        )
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            while (openHandbook.wasPressed()) {
                it.setScreen(EquipmentDictScreen())
            }
        })
    }
}