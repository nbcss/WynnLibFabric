package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import net.minecraft.text.Text

class BasePropertyEffect(json: JsonObject): AbilityEffect {
    companion object {
        val FACTORY = object: AbilityEffect.Factory {
            override fun create(properties: JsonObject): AbilityEffect {
                return BasePropertyEffect(properties)
            }
        }
    }

    private val properties: MutableMap<String, String> = HashMap()
    init {
        for (entry in json.entrySet()) {
            properties[entry.key] = AbilityProperty.encode(entry.key, entry.value)
        }
    }

    override fun getEffectTooltip(): List<Text> {
        return emptyList()
    }

    override fun getProperty(key: String): String {
        return properties.getOrDefault(key, "")
    }

    override fun hasProperty(key: String): Boolean {
        return properties.contains(key)
    }
}