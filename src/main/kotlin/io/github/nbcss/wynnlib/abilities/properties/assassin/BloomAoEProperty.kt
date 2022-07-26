package io.github.nbcss.wynnlib.abilities.properties.assassin

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_ASSASSIN_BLOOM_TIP
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class BloomAoEProperty(ability: Ability,
                       private val range: Double,
                       private val maxRange: Double): AbilityProperty(ability), SetupProperty {
    companion object: Type<BloomAoEProperty> {
        private const val RANGE_KEY: String = "range"
        private const val MAX_RANGE_KEY: String = "max_range"
        override fun create(ability: Ability, data: JsonElement): BloomAoEProperty {
            val json = data.asJsonObject
            val range = if (json.has(RANGE_KEY)) json[RANGE_KEY].asDouble else 0.0
            val maxRange = if (json.has(MAX_RANGE_KEY)) json[MAX_RANGE_KEY].asDouble else 0.0
            return BloomAoEProperty(ability, range, maxRange)
        }
        override fun getKey(): String = "bloom_aoe"
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }


    override fun getTooltip(): List<Text> {
        val suffix = if(range <= 1)
            Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS
        return listOf(Symbol.AOE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_AREA_OF_EFFECT.formatted(Formatting.GRAY).append(": "))
            .append(suffix.formatted(Formatting.WHITE, null, "+${removeDecimal(range)}"))
            .append(LiteralText(" (${TOOLTIP_ABILITY_ASSASSIN_BLOOM_TIP.translate().string})")
                .formatted(Formatting.DARK_GRAY)),
            LiteralText("   (").formatted(Formatting.DARK_GRAY)
            .append(Symbol.MAX.asText()).append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.DARK_GRAY))
            .append(": ").append(suffix.formatted(Formatting.DARK_GRAY, null, removeDecimal(maxRange)))
                .append(")"))
    }
}