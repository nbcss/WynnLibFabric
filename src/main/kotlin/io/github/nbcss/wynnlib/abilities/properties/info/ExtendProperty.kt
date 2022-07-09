package io.github.nbcss.wynnlib.abilities.properties.info

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.data.SpellSlot

class ExtendProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Type {
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return ExtendProperty(ability, data)
        }
        override fun getKey(): String = "extend"
        private const val SPELL_KEY = "spell"
        private const val ABILITY_KEY = "ability"
    }
    private val spell: SpellSlot?
    private val name: String?

    init {
        val json = data.asJsonObject
        spell = if (json.has(SPELL_KEY)) SpellSlot.fromName(json[SPELL_KEY].asString) else null
        name = if (json.has(ABILITY_KEY)) json[ABILITY_KEY].asString else null
    }

    fun getParent(container: EntryContainer): PropertyEntry? {
        if (spell != null){
            val entry = container.getEntry(spell.name)
            if (entry != null && name != null && entry.getAbility().getKey() != name) {
                return null
            }
            return entry
        }
        if (name != null){
            return container.getEntry(name)
        }
        return null
    }
}