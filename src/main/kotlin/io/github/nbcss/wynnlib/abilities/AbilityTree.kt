package io.github.nbcss.wynnlib.abilities

import io.github.nbcss.wynnlib.data.CharacterClass
import kotlin.math.max

class AbilityTree(val character: CharacterClass) {
    private val abilities: MutableSet<Ability> = HashSet()
    private var height: Int = 0

    fun getArchetypes(): Array<Archetype> {
        return emptyArray()
    }

    fun getMaxHeight(): Int = height

    fun getAbilities(): Collection<Ability> {
        return abilities
    }

    fun setAbilities(abilities: Collection<Ability>) {
        this.abilities.clear()
        this.height = 0
        abilities.forEach {
            this.abilities.add(it)
            this.height = max(this.height, it.getHeight())
        }
    }
}