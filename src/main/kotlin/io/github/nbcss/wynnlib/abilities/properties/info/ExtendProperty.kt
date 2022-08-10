package io.github.nbcss.wynnlib.abilities.properties.info

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.ValidatorProperty
import io.github.nbcss.wynnlib.data.SpellSlot

class ExtendProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Type<ExtendProperty> {
        override fun create(ability: Ability, data: JsonElement): ExtendProperty {
            return ExtendProperty(ability, data)
        }
        override fun getKey(): String = "extend"
        private const val SPELL_KEY = "spell"
        private const val ABILITY_KEY = "ability"
    }
    private val spell: String?
    private val name: String?
    init {
        val json = data.asJsonObject
        spell = if (json.has(SPELL_KEY)) json[SPELL_KEY].asString else null
        name = if (json.has(ABILITY_KEY)) json[ABILITY_KEY].asString else null
    }

    private fun validateEntry(container: EntryContainer): Boolean {
        for (property in getAbility().getProperties()) {
            if (property is ValidatorProperty){
                if (!property.validate(container))
                    return false
            }
        }
        return true
    }

    fun getParent(container: EntryContainer): PropertyEntry? {
        //at least one dependency need presents
        if (!validateEntry(container)) {
            return null
        }
        if (name != null){
            return container.getEntry(name)
        }else if (spell != null){
            return container.getSlotEntry(spell).firstOrNull()
        }
        return null
    }
}