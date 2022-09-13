package io.github.nbcss.wynnlib.abilities.properties.shaman

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.OverviewProvider
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.abilities.properties.general.MainAttackDamageProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_SHAMAN_BEAMS_DAMAGE
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.colorOf
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ShamanBeamProperty(ability: Ability,
                         private val beams: Int):
    AbilityProperty(ability), SetupProperty, OverviewProvider {
    companion object: Type<ShamanBeamProperty> {
        override fun create(ability: Ability, data: JsonElement): ShamanBeamProperty {
            return ShamanBeamProperty(ability, data.asInt)
        }
        override fun getKey(): String = "shaman_beams"
    }

    fun getBeams(): Int = beams

    fun getBeamDamage(provider: PropertyProvider): Int {
        val damage = MainAttackDamageProperty.from(provider)?.getMainAttackDamage() ?: 100
        return damage / beams
    }

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), "$beams")
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getOverviewTip(): Text? {
        return Symbol.ALTER_HITS.asText().append(" ").append(
            LiteralText("$beams").formatted(Formatting.WHITE)
        )
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val damage = TOOLTIP_ABILITY_SHAMAN_BEAMS_DAMAGE.translate(label = null, getBeamDamage(provider)).string
        return listOf(Symbol.ALTER_HITS.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_SHAMAN_BEAMS.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("$beams").formatted(Formatting.WHITE))
            .append(LiteralText(" ($damage)").formatted(Formatting.DARK_GRAY)))
    }

    class Modifier(ability: Ability, private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "shaman_beams_modifier"
        }

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), "$modifier")
        }

        override fun modify(entry: PropertyEntry) {
            ShamanBeamProperty.from(entry)?.let {
                val beams = it.getBeams() + modifier
                entry.setProperty(ShamanBeamProperty.getKey(), ShamanBeamProperty(it.getAbility(), beams))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            return listOf(Symbol.ALTER_HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_SHAMAN_BEAMS.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(signed(modifier)).formatted(colorOf(modifier))))
        }
    }
}