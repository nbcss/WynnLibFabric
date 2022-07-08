package io.github.nbcss.wynnlib.abilities.properties

import io.github.nbcss.wynnlib.abilities.builder.PropertyEntry

interface UpgradeableProperty {
    fun upgrade(entry: PropertyEntry)
}