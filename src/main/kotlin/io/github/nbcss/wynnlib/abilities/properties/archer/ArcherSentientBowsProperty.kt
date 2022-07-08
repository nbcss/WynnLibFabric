package io.github.nbcss.wynnlib.abilities.properties.archer

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ArcherSentientBowsProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return ArcherSentientBowsProperty(ability, data)
        }
        override fun getKey(): String = "archer_sentient_bows"
    }
    private val bows: Int = data.asInt
    init {
        ability.putPlaceholder(getKey(), bows.toString())
    }

    fun getArcherSentientBows(): Int = bows

    override fun getTooltip(): List<Text> {
        return listOf(Symbol.ALTER_HITS.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_ARCHER_SENTIENT_BOWS.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(bows.toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, data: JsonElement): AbilityProperty(ability) {
        companion object: Factory {
            override fun create(ability: Ability, data: JsonElement): AbilityProperty {
                return Modifier(ability, data)
            }
            override fun getKey(): String = "archer_sentient_bows_modifier"
        }
        private val modifier: Int = data.asInt
        init {
            ability.putPlaceholder(getKey(), modifier.toString())
        }

        fun getArcherSentientBowsModifier(): Int = modifier

        override fun getTooltip(): List<Text> {
            return listOf(Symbol.ALTER_HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_ARCHER_SENTIENT_BOWS.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(signed(modifier)).formatted(Formatting.WHITE)))
        }
    }
}