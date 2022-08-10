package io.github.nbcss.wynnlib.abilities.properties.mage

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.abilities.properties.general.ManaCostProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.colorOf
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MageManaBankProperty(ability: Ability,
                           private val size: Int): AbilityProperty(ability), SetupProperty {
    companion object: Type<MageManaBankProperty> {
        override fun create(ability: Ability, data: JsonElement): MageManaBankProperty {
            return MageManaBankProperty(ability, data.asInt)
        }
        override fun getKey(): String = "mana_bank"
    }

    fun getManaBankSize(): Int = size

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), size.toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val bank = Translations.TOOLTIP_ABILITY_MAGE_MANA_BANK.translate().string
        return listOf(Symbol.MANA.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${bank}): "))
            .append(LiteralText(getManaBankSize().toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "mana_bank_modifier"
        }

        fun getManaModifier(): Int = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), modifier.toString())
        }

        override fun modify(entry: PropertyEntry) {
            MageManaBankProperty.from(entry)?.let {
                val cost = it.getManaBankSize() + getManaModifier()
                entry.setProperty(MageManaBankProperty.getKey(), MageManaBankProperty(it.getAbility(), cost))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            val bank = Translations.TOOLTIP_ABILITY_MAGE_MANA_BANK.translate().string
            return listOf(Symbol.MANA.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${bank}): "))
                .append(LiteralText(signed(modifier)).formatted(colorOf(modifier))))
        }
    }
}