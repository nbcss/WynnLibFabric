package io.github.nbcss.wynnlib

import com.google.gson.JsonParser
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.gui.dicts.EquipmentDictScreen
import io.github.nbcss.wynnlib.registry.*
import io.github.nbcss.wynnlib.utils.FileUtils
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
    private const val ID_RESOURCE = "assets/wynnlib/data/Identifications.json"
    private const val POWDER_RESOURCE = "assets/wynnlib/data/Powders.json"
    private const val EQUIPMENT_RESOURCE = "assets/wynnlib/data/Equipments.json"
    private const val INGREDIENT_RESOURCE = "assets/wynnlib/data/Ingredients.json"
    private const val ABILITY_RESOURCE = "assets/wynnlib/data/Abilities.json"
    private const val MATERIAL_RESOURCE = "assets/wynnlib/data/Materials.json"

    override fun onInitialize() {
        //Reload id metadata
        Identification.reload(JsonParser.parseReader(InputStreamReader(
            FileUtils.getResource(ID_RESOURCE)!!, "utf-8")).asJsonObject)
        //Load database
        FileUtils.loadRegistry(PowderRegistry, POWDER_RESOURCE)
        FileUtils.loadRegistry(RegularEquipmentRegistry, EQUIPMENT_RESOURCE)
        FileUtils.loadRegistry(IngredientRegistry, INGREDIENT_RESOURCE)
        FileUtils.loadRegistry(AbilityRegistry, ABILITY_RESOURCE)
        FileUtils.loadRegistry(MaterialRegistry, MATERIAL_RESOURCE)
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