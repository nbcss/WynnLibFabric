package io.github.nbcss.wynnlib

import com.google.gson.JsonParser
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.gui.dicts.EquipmentDictScreen
import io.github.nbcss.wynnlib.registry.IngredientRegistry
import io.github.nbcss.wynnlib.registry.PowderRegistry
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
        //Reload id metadata
        Identification.reload(JsonParser.parseReader(InputStreamReader(
            getResource("assets/wynnlib/data/Identifications.json")!!, "utf-8")).asJsonObject)
        //Load database
        PowderRegistry.reload(JsonParser.parseReader(InputStreamReader(
            getResource("assets/wynnlib/data/Powders.json")!!, "utf-8")).asJsonObject)
        RegularEquipmentRegistry.reload(JsonParser.parseReader(InputStreamReader(
            getResource("assets/wynnlib/data/Equipments.json")!!, "utf-8")).asJsonObject)
        IngredientRegistry.reload(JsonParser.parseReader(InputStreamReader(
            getResource("assets/wynnlib/data/Ingredients.json")!!, "utf-8")).asJsonObject)
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
                it.setScreen(EquipmentDictScreen(it.currentScreen))
            }
        })
    }
}