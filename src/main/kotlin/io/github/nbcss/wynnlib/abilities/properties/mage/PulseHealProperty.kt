package io.github.nbcss.wynnlib.abilities.properties.mage

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class PulseHealProperty(ability: Ability,
                        private val heal: Int,
                        private val trigger: Int):
    AbilityProperty(ability), ModifiableProperty {
    companion object: Type<PulseHealProperty> {
        override fun create(ability: Ability, data: JsonElement): PulseHealProperty {
            val json = data.asJsonObject
            val heal = json["heal"].asInt
            val trigger = json["trigger"].asInt
            return PulseHealProperty(ability, heal, trigger)
        }
        override fun getKey(): String = "pulse_heal"
    }

    fun getPulseHeal(): Int = heal

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("trigger", trigger.toString())
    }

    override fun modify(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        return listOf(Symbol.HEART.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_PULSE_HEAL.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("$heal%").formatted(Formatting.WHITE))
            .append(LiteralText(" (x$trigger)").formatted(Formatting.DARK_GRAY)))
    }
}