package io.github.nbcss.wynnlib.abilities.properties

import io.github.nbcss.wynnlib.abilities.builder.EntryContainer

interface ValidatorProperty {
    fun validate(container: EntryContainer): Boolean
}