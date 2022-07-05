package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ArcherStreamProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return ArcherStreamProperty(ability, data)
        }
        override fun getKey(): String = "archer_stream"
    }
    private val streams: Int = data.asInt

    fun getArcherStreams(): Int = streams

    override fun getTooltip(): List<Text> {
        return listOf(Symbol.ALTER_HITS.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_ARCHER_STREAM.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(streams.toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, data: JsonElement): AbilityProperty(ability) {
        companion object: Factory {
            override fun create(ability: Ability, data: JsonElement): AbilityProperty {
                return Modifier(ability, data)
            }
            override fun getKey(): String = "archer_stream_modifier"
        }
        private val modifier: Int = data.asInt

        fun getArcherStreamsModifier(): Int = modifier

        override fun getTooltip(): List<Text> {
            return listOf(Symbol.ALTER_HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_ARCHER_STREAM.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(signed(modifier)).formatted(Formatting.WHITE)))
        }
    }
}