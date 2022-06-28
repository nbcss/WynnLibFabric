package io.github.nbcss.wynnlib.abilities.properties

import io.github.nbcss.wynnlib.abilities.PropertyProvider

object RangeProperty: AbilityProperty<Double> {
    const val KEY: String = "range"

    override fun read(provider: PropertyProvider): Double? {
        return provider.getProperty(KEY).toDoubleOrNull()
    }
}