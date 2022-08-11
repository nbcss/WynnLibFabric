package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_MAX
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class TimeDilationProperty(ability: Ability,
                           private val modifier: Int,
                           private val max: Int): AbilityProperty(ability), SetupProperty {
    companion object: Type<TimeDilationProperty> {
        override fun create(ability: Ability, data: JsonElement): TimeDilationProperty {
            val json = data.asJsonObject
            val modifier = if (json.has(MODIFIER_KEY)) json[MODIFIER_KEY].asInt else 0
            val max = if (json.has(MAX_KEY)) json[MAX_KEY].asInt else 0
            return TimeDilationProperty(ability, modifier, max)
        }
        override fun getKey(): String = "time_dilation"
        private val SPEED_EFFECT = BonusEffectProperty.EffectType.ALLIES_WALK_SPEED
        private const val MODIFIER_KEY = "modifier"
        private const val MAX_KEY = "max"
    }

    fun getModifierPerSecond(): Int = modifier

    fun getMaxModifier(): Int = max

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val text = Symbol.EFFECT.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_EFFECT.formatted(Formatting.GRAY).append(": "))
        text.append(Translations.TOOLTIP_SUFFIX_PER_S.formatted(Formatting.WHITE,
            null, "${signed(modifier)}%")).append(" ")
            .append(SPEED_EFFECT.formatted(Formatting.GRAY))
        return listOf(text, LiteralText("   (").formatted(Formatting.DARK_GRAY)
            .append(Symbol.MAX.asText()).append(" ")
            .append(TOOLTIP_ABILITY_MAX.formatted(Formatting.DARK_GRAY))
            .append(": ${signed(max)}%)"))
    }
}