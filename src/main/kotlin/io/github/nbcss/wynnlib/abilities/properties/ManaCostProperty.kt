package io.github.nbcss.wynnlib.abilities.properties

import io.github.nbcss.wynnlib.abilities.PropertyProvider

object ManaCostProperty: AbilityProperty<Int> {
    const val KEY: String = "mana_cost"

    override fun read(provider: PropertyProvider): Int? {
        return provider.getProperty(KEY).toIntOrNull()
    }
}