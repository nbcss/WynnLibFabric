package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import net.minecraft.text.Text

open class BaseEffect(protected val parent: Ability,
                      json: JsonObject): AbilityEffect {
    companion object {
        val FACTORY = object: AbilityEffect.Factory {
            override fun create(parent: Ability, properties: JsonObject): AbilityEffect {
                return BaseEffect(parent, properties)
            }
        }
    }

    private val properties: MutableMap<String, String> = HashMap()
    init {
        for (entry in json.entrySet()) {
            properties[entry.key] = entry.value.toString()
        }
    }

    protected open fun getTooltipItems(): List<EffectTooltip> = emptyList()

    override fun getEffectTooltip(): List<Text> {
        return getTooltipItems().map { it.get(this) }.flatten()
    }

    override fun getPropertyString(key: String): String {
        return properties.getOrDefault(key, key)
    }

    override fun getAbility(): Ability = parent
}