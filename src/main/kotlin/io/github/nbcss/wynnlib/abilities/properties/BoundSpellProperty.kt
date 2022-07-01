package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.SpellSlot

interface BoundSpellProperty {
    companion object {
        const val KEY: String = "spell"

        /**
         * Read spell slot from the json data.
         * If it is not available, return Spell_1
         */
        fun read(data: JsonObject): SpellSlot = if (data.has(KEY))
            SpellSlot.fromName(data[KEY].asString) ?: SpellSlot.SPELL_1 else SpellSlot.SPELL_1
    }

    fun getSpell(): SpellSlot
}