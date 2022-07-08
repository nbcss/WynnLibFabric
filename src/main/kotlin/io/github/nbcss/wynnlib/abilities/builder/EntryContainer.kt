package io.github.nbcss.wynnlib.abilities.builder

import io.github.nbcss.wynnlib.abilities.Ability

class EntryContainer(abilities: Collection<Ability> = emptyList()) {
    private val entries: MutableMap<String, PropertyEntry>
    init {
        entries = LinkedHashMap()
        abilities.sortedBy { it.getPropertyPriorityIndex() }
            .forEach { it.updateEntries(this) }
    }

    fun setEntry(key: String, entry: PropertyEntry) {
        entries[key] = entry
    }

    fun getEntry(key: String): PropertyEntry? {
        return entries[key]
    }

    fun getEntries(): List<PropertyEntry> {
        return entries.values.toList()
    }
}