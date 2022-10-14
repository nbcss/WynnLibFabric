package io.github.nbcss.wynnlib.abilities.properties.shaman

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.OverviewProvider
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import io.github.nbcss.wynnlib.utils.round
import io.github.nbcss.wynnlib.abilities.properties.HealProperty
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class RegenerationProperty(ability: Ability,
                           private val heal: Double,
                           private val interval: Double):
    AbilityProperty(ability), SetupProperty, OverviewProvider, HealProperty {
    companion object: Type<RegenerationProperty> {
        private const val HEAL_KEY = "heal"
        private const val INTERVAL_KEY = "interval"
        override fun create(ability: Ability, data: JsonElement): RegenerationProperty {
            val json = data.asJsonObject
            val heal = if (json.has(HEAL_KEY)) json[HEAL_KEY].asDouble else 0.0
            val interval = if (json.has(INTERVAL_KEY)) json[INTERVAL_KEY].asDouble else 0.0
            return RegenerationProperty(ability, heal, interval)
        }
        override fun getKey(): String = "regeneration"
    }

    fun getHealPerSec(): Double = round(if (interval == 0.0) 0.0 else heal / interval)

    override fun modifyHeal(ability: Ability, modifier: Double): AbilityProperty {
        return RegenerationProperty(ability, round(heal + modifier), interval)
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("regeneration.heal", removeDecimal(heal))
        container.putPlaceholder("regeneration.interval", removeDecimal(interval))
    }

    override fun getOverviewTip(): Text {
        return Symbol.HEART.asText().append(" ").append(
            Translations.TOOLTIP_SUFFIX_PER_S.formatted(Formatting.WHITE,
                null, "${getHealPerSec()}%")
        )
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val suffix = Translations.TOOLTIP_ABILITY_PER_SEC.translate(label = null, removeDecimal(interval)).string
        val healText = Symbol.HEART.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_SHAMAN_REGENERATION.formatted(Formatting.GRAY))
            .append(LiteralText(": ").formatted(Formatting.GRAY))
            .append(LiteralText("+${removeDecimal(heal)}%").formatted(Formatting.WHITE))
            .append(LiteralText(" ($suffix)").formatted(Formatting.DARK_GRAY))
        val healPerSec = Translations.TOOLTIP_SUFFIX_PER_S.formatted(Formatting.WHITE,
            null, "+${getHealPerSec()}%")
        val healPerSecText = LiteralText("   (").formatted(Formatting.GRAY)
            .append(healPerSec).append(" ").append(Symbol.HEART.asText()).append(")")
        return listOf(healText, healPerSecText)
    }
}