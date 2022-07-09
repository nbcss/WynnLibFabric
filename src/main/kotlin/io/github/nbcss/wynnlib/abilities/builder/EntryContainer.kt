package io.github.nbcss.wynnlib.abilities.builder

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.info.EntryProperty

class EntryContainer(abilities: Collection<Ability> = emptyList()) {
    private val entries: MutableMap<String, PropertyEntry>
    init {
        entries = LinkedHashMap()
        val spells: MutableSet<Ability> = HashSet()

        for (ability in abilities) {
            val entry = ability.getProperty(EntryProperty.getKey())
            if (entry is EntryProperty){
                //todo
            }
        }
        /*abilities.sortedBy { it.getPropertyPriorityIndex() }.forEach {
            it.updateEntries(this)
        }*/
    }

    fun putEntry(entry: PropertyEntry) {
        //println("put " + entry.getAbility().getKey())
        entries[entry.getKey()] = entry
    }

    fun getEntry(key: String): PropertyEntry? {
        return entries[key]
    }

    fun getEntries(): List<PropertyEntry> {
        return entries.values.toList()
    }
}