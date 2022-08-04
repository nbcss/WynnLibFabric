package io.github.nbcss.wynnlib.abilities.properties.mage

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class TeleportSuccessionProperty(ability: Ability,
                                 private val succession: Int):
    AbilityProperty(ability), ModifiableProperty {
    companion object: Type<TeleportSuccessionProperty> {
        override fun create(ability: Ability, data: JsonElement): TeleportSuccessionProperty {
            return TeleportSuccessionProperty(ability, data.asInt)
        }
        override fun getKey(): String = "tp_successions"
    }

    fun getSuccessions(): Int = succession

    override fun modify(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        return listOf(Symbol.ALTER_HITS.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAGE_BLINKS.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("$succession").formatted(Formatting.WHITE)))
    }
}