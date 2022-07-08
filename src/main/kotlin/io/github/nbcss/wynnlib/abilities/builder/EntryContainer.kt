package io.github.nbcss.wynnlib.abilities.builder

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry

class EntryContainer(abilities: Collection<Ability> = emptyList()) {
    private val entries: MutableMap<String, PropertyEntry>
    init {
        entries = LinkedHashMap()
        abilities.sortedBy { it.getPropertyPriorityIndex() }
            .forEach { it.updateEntries(this) }
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