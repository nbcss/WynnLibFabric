package io.github.nbcss.wynnlib

import io.github.nbcss.wynnlib.gui.dicts.EquipmentDictScreen
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object WynnLibKeybindings {
    private val lockSlot = registerKey("wynnlib.key.slot_lock", GLFW.GLFW_KEY_L)
    private val openHandbook = registerKey("wynnlib.key.handbook", GLFW.GLFW_KEY_H)
    fun init() {
        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick {
            while (openHandbook.wasPressed()) {
                it.setScreen(EquipmentDictScreen(it.currentScreen))
            }
        })
    }

    fun getLockSlotKeybinding(): KeyBinding = lockSlot

    private fun registerKey(name: String, key: Int): KeyBinding {
        return KeyBindingHelper.registerKeyBinding(
            KeyBinding(name, InputUtil.Type.KEYSYM, key, "wynnlib.category.keys")
        )
    }
}