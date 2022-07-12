package io.github.nbcss.wynnlib.abilities.properties.info

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.data.SpellSlot

open class ModifyProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Type<ModifyProperty> {
        override fun create(ability: Ability, data: JsonElement): ModifyProperty {
            return ModifyProperty(ability, data)
        }
        override fun getKey(): String = "modify"
        private const val SPELL_KEY = "spell"
        private const val ABILITY_KEY = "ability"
        private const val EXTEND_KEY = "extends"
    }
    private val spell: SpellSlot?
    private val name: String?
    private val extends: List<String>

    init {
        val json = data.asJsonObject
        spell = if (json.has(SPELL_KEY)) SpellSlot.fromName(json[SPELL_KEY].asString) else null
        name = if (json.has(ABILITY_KEY)) json[ABILITY_KEY].asString else null
        extends = if(json.has(EXTEND_KEY)) json[EXTEND_KEY].asJsonArray.map { it.asString } else emptyList()
    }

    fun getModifyEntries(container: EntryContainer): List<PropertyEntry> {
        val entries: MutableList<PropertyEntry> = mutableListOf()
        if (spell != null){
            val entry = container.getEntry(spell.name)
            if (entry != null){
                if (name == null || entry.getAbility().getKey() == name){
                    entries.add(entry)
                }
            }
        }else if (name != null){
            container.getEntry(name)?.let { entries.add(it) }
        }
        if (entries.isNotEmpty()){
            extends.mapNotNull { container.getEntry(it) }.forEach { entries.add(it) }
        }
        return entries
    }

    fun modifyEntry(entry: PropertyEntry){
        for (property in getAbility().getProperties()) {
            if (property is ModifiableProperty){
                property.modify(entry)
            }
        }
    }

    override fun updateEntries(container: EntryContainer) {
        getModifyEntries(container).forEach { modifyEntry(it) }
    }
}