package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import net.minecraft.text.Text

class PropertyHolderEffect(json: JsonObject): AbilityEffect {
    companion object {
        val FACTORY = object: AbilityEffect.Factory {
            override fun create(properties: JsonObject): AbilityEffect {
                return PropertyHolderEffect(properties)
            }
        }
    }

    private val properties: MutableMap<String, String> = HashMap()
    init {
        for (entry in json.entrySet()) {
            properties[entry.key] = entry.value.asString
        }
    }

    override fun getEffectTooltip(): List<Text> {
        return emptyList()
    }

    override fun getProperty(key: String): String {
        return properties.getOrDefault(key, key)
    }
}