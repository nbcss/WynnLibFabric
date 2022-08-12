package io.github.nbcss.wynnlib.abilities.properties

import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry

interface SetupProperty {
    fun setup(entry: PropertyEntry)
    fun inUpgrade(): Boolean = true
}