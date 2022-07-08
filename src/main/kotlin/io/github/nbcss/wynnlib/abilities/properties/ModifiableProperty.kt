package io.github.nbcss.wynnlib.abilities.properties

import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry

interface ModifiableProperty {
    fun modify(entry: PropertyEntry)
}