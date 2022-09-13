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
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.colorOf
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ShamanBloodPoolProperty(ability: Ability,
                              private val size: Int):
    AbilityProperty(ability), SetupProperty, OverviewProvider {
    companion object: Type<ShamanBloodPoolProperty> {
        override fun create(ability: Ability, data: JsonElement): ShamanBloodPoolProperty {
            return ShamanBloodPoolProperty(ability, data.asInt)
        }
        override fun getKey(): String = "blood_pool"
    }

    fun getBloodPoolSize(): Int = size

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), size.toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getOverviewTip(): Text? {
        return Symbol.DARK_HEART.asText().append(" ").append(
            LiteralText("$size%").formatted(Formatting.WHITE)
        )
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val name = Translations.TOOLTIP_ABILITY_SHAMAN_BLOOD_POOL.translate().string
        return listOf(Symbol.DARK_HEART.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${name}): "))
            .append(LiteralText("$size%").formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "blood_pool_modifier"
        }

        fun getModifier(): Int = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), modifier.toString())
        }

        override fun modify(entry: PropertyEntry) {
            ShamanBloodPoolProperty.from(entry)?.let {
                val value = it.getBloodPoolSize() + getModifier()
                entry.setProperty(ShamanBloodPoolProperty.getKey(), ShamanBloodPoolProperty(it.getAbility(), value))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            val name = Translations.TOOLTIP_ABILITY_SHAMAN_BLOOD_POOL.translate().string
            return listOf(Symbol.DARK_HEART.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_MAX.formatted(Formatting.GRAY).append(" (${name}): "))
                .append(LiteralText("${signed(modifier)}%").formatted(colorOf(modifier))))
        }
    }
}