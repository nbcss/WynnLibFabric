package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.SpellSlot

interface BoundSpellProperty {
    companion object {
        const val KEY: String = "spell"
        fun read(data: JsonObject): SpellSlot = SpellSlot.fromName(data[KEY].asString)!!
    }

    fun getSpell(): SpellSlot
}