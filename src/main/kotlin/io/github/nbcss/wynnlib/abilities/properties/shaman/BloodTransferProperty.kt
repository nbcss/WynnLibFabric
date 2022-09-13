package io.github.nbcss.wynnlib.abilities.properties.shaman

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_SHAMAN_BLOOD_TRANSFER
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.removeDecimal
import io.github.nbcss.wynnlib.utils.round
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class BloodTransferProperty(ability: Ability,
                            private val rate: Double,
                            private val interval: Double): AbilityProperty(ability), SetupProperty {
    companion object: Type<BloodTransferProperty> {
        private const val RATE_KEY = "rate"
        private const val INTERVAL_KEY = "interval"
        override fun create(ability: Ability, data: JsonElement): BloodTransferProperty {
            val json = data.asJsonObject
            val rate = if (json.has(RATE_KEY)) json[RATE_KEY].asDouble else 0.0
            val interval = if (json.has(INTERVAL_KEY)) json[INTERVAL_KEY].asDouble else 0.0
            return BloodTransferProperty(ability, rate, interval)
        }
        override fun getKey(): String = "blood_transfer"
    }

    fun getTransferRate(): Double = rate

    fun getTransferInterval(): Double = interval

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder("blood_transfer.rate", removeDecimal(rate))
        container.putPlaceholder("blood_transfer.interval", removeDecimal(interval))
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val modifier = removeDecimal(round(if (interval == 0.0) 0.0 else rate / interval))
        return listOf(Symbol.HEART.asText().append(" ")
            .append(TOOLTIP_ABILITY_SHAMAN_BLOOD_TRANSFER.formatted(Formatting.GRAY))
            .append(LiteralText(": ").formatted(Formatting.GRAY))
            .append(Translations.TOOLTIP_SUFFIX_PER_S.formatted(Formatting.WHITE,
                null, "+${modifier}%")))
    }
}