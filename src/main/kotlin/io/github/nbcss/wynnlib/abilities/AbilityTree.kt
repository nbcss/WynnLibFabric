package io.github.nbcss.wynnlib.abilities

import io.github.nbcss.wynnlib.data.CharacterClass

class AbilityTree(val character: CharacterClass) {
    private val abilities: MutableSet<Ability> = HashSet()

    fun getArchetypes(): Array<Archetype> {
        return emptyArray()
    }

    fun getAbilities(): Collection<Ability> {
        return abilities
    }

    fun setAbilities(abilities: Collection<Ability>) {
        this.abilities.clear()
        this.abilities.addAll(abilities)
    }
}