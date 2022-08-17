package io.github.nbcss.wynnlib.abilities.properties.mage

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PlaceholderContainer
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_MAGE_MANA_STORE
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.colorOf
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class MageManaStoreProperty(ability: Ability,
                            private val store: Int): AbilityProperty(ability), SetupProperty {
    companion object: Type<MageManaStoreProperty> {
        override fun create(ability: Ability, data: JsonElement): MageManaStoreProperty {
            return MageManaStoreProperty(ability, data.asInt)
        }
        override fun getKey(): String = "mana_storing"
    }

    fun getManaStore(): Int = store

    override fun writePlaceholder(container: PlaceholderContainer) {
        container.putPlaceholder(getKey(), store.toString())
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        return listOf(Symbol.MANA.asText().append(" ")
            .append(TOOLTIP_ABILITY_MAGE_MANA_STORE.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("+$store").formatted(Formatting.WHITE)))
    }

    class Modifier(ability: Ability, private val modifier: Int):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data.asInt)
            }
            override fun getKey(): String = "mana_storing_modifier"
        }

        fun getManaModifier(): Int = modifier

        override fun writePlaceholder(container: PlaceholderContainer) {
            container.putPlaceholder(getKey(), modifier.toString())
        }

        override fun modify(entry: PropertyEntry) {
            MageManaStoreProperty.from(entry)?.let {
                val value = it.getManaStore() + getManaModifier()
                entry.setProperty(MageManaStoreProperty.getKey(), MageManaStoreProperty(it.getAbility(), value))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
            return listOf(Symbol.MANA.asText().append(" ")
                .append(TOOLTIP_ABILITY_MAGE_MANA_STORE.formatted(Formatting.GRAY).append(": "))
                .append(LiteralText(signed(modifier)).formatted(colorOf(modifier))))
        }
    }
}