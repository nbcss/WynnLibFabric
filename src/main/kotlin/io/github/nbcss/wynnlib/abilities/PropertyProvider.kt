package io.github.nbcss.wynnlib.abilities

import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty

interface PropertyProvider {
    fun getProperty(key: String): AbilityProperty?
}