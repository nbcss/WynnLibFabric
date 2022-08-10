package io.github.nbcss.wynnlib.abilities.properties.info

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ReplaceAbilityProperty(ability: Ability,
                             private val id: String,
                             private val keepProperties: List<String>): AbilityProperty(ability) {
    companion object: Type<ReplaceAbilityProperty> {
        private const val ID_KEY = "ability"
        private const val PROPERTIES_KEY = "properties"
        override fun create(ability: Ability, data: JsonElement): ReplaceAbilityProperty {
            val json = data.asJsonObject
            val id = if (json.has(ID_KEY)) json[ID_KEY].asString else ""
            val properties = if (json.has(PROPERTIES_KEY))
                json[PROPERTIES_KEY].asJsonArray.map { it.asString } else emptyList()
            return ReplaceAbilityProperty(ability, id, properties)
        }
        override fun getKey(): String = "replacing"
    }

    fun getKeepProperties(): List<Type<out AbilityProperty>> {
        return keepProperties.mapNotNull { getType(it) }
    }

    fun getReplaceAbility(): Ability? {
        return AbilityRegistry.get(id)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        getReplaceAbility()?.let {
            return listOf(Symbol.REPLACE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_REPLACING.formatted(Formatting.GRAY))
                .append(LiteralText(": ").formatted(Formatting.GRAY))
                .append(it.formatted(Formatting.WHITE)))
        }
        return emptyList()
    }
}