package io.github.nbcss.wynnlib

import com.google.gson.JsonParser
import io.github.nbcss.wynnlib.data.Metadata
import io.github.nbcss.wynnlib.gui.EquipmentDictScreen
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry
import io.github.nbcss.wynnlib.utils.getResource
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW
import java.io.InputStreamReader


@Suppress("UNUSED")
object WynnLibEntry: ModInitializer {
    private const val MOD_ID = "wynnlib"

    override fun onInitialize() {
        //Reload metadata
        Metadata.reload(JsonParser.parseReader(InputStreamReader(
            getResource("assets/wynnlib/data/Metadata.json")!!, "utf-8")).asJsonObject)
        //Load database
        RegularEquipmentRegistry.reload(JsonParser.parseReader(InputStreamReader(
            getResource("assets/wynnlib/data/Equipments.json")!!, "utf-8")).asJsonObject)
        //Register keybindings
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