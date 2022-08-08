package io.github.nbcss.wynnlib.abilities.properties.mage

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.abilities.properties.ValidatorProperty
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_WINDED_BOOSTERS
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class WindedBoosterProperty(ability: Ability,
                            private val dependencies: List<String>):
    AbilityProperty(ability), ValidatorProperty, SetupProperty {
    companion object: Type<WindedBoosterProperty> {
        override fun create(ability: Ability, data: JsonElement): WindedBoosterProperty {
            return WindedBoosterProperty(ability, data.asJsonArray.map { it.asString })
        }
        override fun getKey(): String = "winded_booster"
    }

    override fun validate(container: EntryContainer): Boolean {
        return dependencies.any { container.getEntry(it) != null }
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        tooltip.add(LiteralText.EMPTY)
        tooltip.add(TOOLTIP_ABILITY_WINDED_BOOSTERS.formatted(Formatting.AQUA))
        for (dependency in dependencies) {
            val ability = AbilityRegistry.get(dependency)
            if (ability != null) {
                var color = Formatting.GRAY
                if (provider is PropertyEntry) {
                    if (provider.getContainer().getEntry(dependency) == null) {
                        color = Formatting.DARK_GRAY
                    }
                }
                tooltip.add(LiteralText("- ").formatted(Formatting.AQUA)
                    .append(ability.translate().formatted(color)))
            }
        }
        return tooltip
    }

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }
}