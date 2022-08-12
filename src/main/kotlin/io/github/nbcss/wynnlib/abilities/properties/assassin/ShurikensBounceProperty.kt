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

class ShurikensBounceProperty(ability: Ability,
                              private val bounce: Int):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<ShurikensBounceProperty> {
        override fun create(ability: Ability, data: JsonElement): ShurikensBounceProperty {
            return ShurikensBounceProperty(ability, data.asInt)
        }
        override fun getKey(): String = "shurikens_bounce"
    }

    fun getBounces(): Int = bounce

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), bounce.toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        if (bounce <= 0)
            return emptyList()
        return listOf(Symbol.ALTER_HITS.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_ASSASSIN_SHURIKENS_BOUNCE.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText(bounce.toString()).formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability,
                   private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "shurikens_bounce_modifier"
        }

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), modifier.toString())
        }

        fun getModifier(): Int = modifier

        override fun modify(entry: PropertyEntry) {
            ShurikensBounceProperty.from(entry)?.let {
                val value = it.getBounces() + getModifier()
                entry.setProperty(ShurikensBounceProperty.getKey(), ShurikensBounceProperty(it.getAbility(), value))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            return listOf(Symbol.ALTER_HITS.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_ASSASSIN_SHURIKENS_BOUNCE.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(signed(modifier)).formatted(colorOf(modifier))))
        }
    }
}