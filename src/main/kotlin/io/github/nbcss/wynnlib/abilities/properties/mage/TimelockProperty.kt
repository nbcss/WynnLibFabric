package io.github.nbcss.wynnlib.abilities.properties.mage

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_MAGE_TIMELOCK_TIP
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_SUFFIX_S
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class TimelockProperty(ability: Ability,
                       private val duration: Int,
                       private val max: Int,
                       private val perWinded: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<TimelockProperty> {
        override fun create(ability: Ability, data: JsonElement): TimelockProperty {
            val json = data.asJsonObject
            val duration = if (json.has("duration")) json["duration"].asInt else 0
            val max = if (json.has("max")) json["max"].asInt else 0
            val perWinded = if (json.has("per_winded")) json["per_winded"].asInt else 0
            return TimelockProperty(ability, duration, max, perWinded)
        }
        override fun getKey(): String = "timelock"
    }

    fun getDuration(): Int = duration

    fun getMaxDuration(): Int = max

    fun getPerWinded(): Int = perWinded

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val value = TOOLTIP_SUFFIX_S.formatted(Formatting.WHITE, null, signed(duration))
        //TOOLTIP_SUFFIX_S.formatted(Formatting.WHITE, null, max)
        return listOf(Symbol.DURATION.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_DURATION.formatted(Formatting.GRAY).append(": "))
            .append(value).append(LiteralText(" (").formatted(Formatting.DARK_GRAY))
            .append(TOOLTIP_ABILITY_MAGE_TIMELOCK_TIP.formatted(Formatting.DARK_GRAY, label = null, perWinded))
            .append(LiteralText(")").formatted(Formatting.DARK_GRAY)),
            LiteralText("   (").formatted(Formatting.DARK_GRAY)
                .append(Symbol.MAX.asText()).append(" ")
                .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.DARK_GRAY))
                .append(": ").append(TOOLTIP_SUFFIX_S.formatted(Formatting.DARK_GRAY, null, max)).append(")"))
    }
}