package io.github.nbcss.wynnlib.abilities.builder

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityMetadata
import io.github.nbcss.wynnlib.abilities.builder.entries.*
import io.github.nbcss.wynnlib.abilities.properties.info.BoundSpellProperty

class EntryContainer(abilities: Collection<Ability> = emptyList()) {
    private val entries: MutableMap<String, PropertyEntry>
    private val disabled: MutableSet<Ability> = mutableSetOf()
    init {
        entries = LinkedHashMap()
        val spells: MutableSet<AbilityMetadata> = HashSet()
        val replaces: MutableSet<AbilityMetadata> = HashSet()
        val extending: MutableSet<AbilityMetadata> = HashSet()
        val dummy: MutableSet<AbilityMetadata> = HashSet()
        for (ability in abilities) {
            ability.getMetadata()?.let {
                when (it.getFactory()) {
                    is MainAttackEntry.Companion -> {
                        it.createEntry(this)?.let { x -> putEntry(x) }
                    }
                    is SpellEntry.Companion -> {
                        spells.add(it)
                    }
                    is ReplaceSpellEntry.Companion -> {
                        replaces.add(it)
                    }
                    is ExtendEntry.Companion -> {
                        extending.add(it)
                    }
                    else -> {
                        dummy.add(it)
                    }
                }
            }
        }
        spells.sortedBy { BoundSpellProperty.from(it.ability)?.getSpell()?.ordinal ?: 99 }
            .mapNotNull {it.createEntry(this)}.forEach { putEntry(it) }
        replaces.mapNotNull {it.createEntry(this)}.forEach { putEntry(it) }
        dummy.mapNotNull {it.createEntry(this)}.forEach { putEntry(it) }
        var keys: List<AbilityMetadata>
        for (metadata in extending) {
            println(metadata.ability.getKey())
        }
        do {
            keys = extending.toList()
            for (meta in keys) {
                val entry = meta.createEntry(this)
                if (entry != null){
                    putEntry(entry)
                    extending.remove(meta)
                }
            }
        }while (extending.isNotEmpty() && extending.size < keys.size)
        disabled.addAll(extending.map { it.ability })
        for (ability in abilities) {
            if (!ability.updateEntries(this)) {
                disabled.add(ability)
            }
        }
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

    fun isAbilityDisabled(ability: Ability): Boolean {
        return ability in disabled
    }

    fun getSize(): Int = entries.size
}