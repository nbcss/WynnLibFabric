package io.github.nbcss.wynnlib.abilities.properties.assassin

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.colorOf
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class AssassinClonesProperty(ability: Ability,
                             private val clones: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<AssassinClonesProperty> {
        override fun create(ability: Ability, data: JsonElement): AssassinClonesProperty {
            return AssassinClonesProperty(ability, data.asInt)
        }
        override fun getKey(): String = "clones"
    }

    fun getClones(): Int = clones

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), clones.toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
            return listOf(Symbol.CHARGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_ASSASSIN_CLONES.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(clones.toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability,
                   private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "clones_modifier"
        }

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), modifier.toString())
        }

        fun getModifier(): Int = modifier

        override fun modify(entry: PropertyEntry) {
            AssassinClonesProperty.from(entry)?.let {
                val value = it.getClones() + getModifier()
                entry.setProperty(AssassinClonesProperty.getKey(), AssassinClonesProperty(it.getAbility(), value))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            return listOf(Symbol.CHARGE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_ASSASSIN_CLONES.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(signed(modifier)).formatted(colorOf(modifier))))
        }
    }
}