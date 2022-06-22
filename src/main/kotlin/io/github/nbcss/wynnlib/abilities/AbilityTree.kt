package io.github.nbcss.wynnlib.abilities

import io.github.nbcss.wynnlib.data.CharacterClass
import java.util.*
import kotlin.collections.HashSet
import kotlin.math.max

class AbilityTree(val character: CharacterClass) {
    private val archetypes: List<Archetype> = Archetype.values().filter { it.getCharacter() == character }.toList()
    private val archetypePoints: MutableMap<Archetype, Int> = EnumMap(Archetype::class.java)
    private val abilities: MutableSet<Ability> = HashSet()
    private var height: Int = 0

    fun getArchetypes(): List<Archetype> = archetypes

    fun getArchetypePoint(archetype: Archetype): Int = archetypePoints.getOrDefault(archetype, 0)

    fun getMaxHeight(): Int = height

    fun getAbilities(): Collection<Ability> {
        return abilities
    }

    fun setAbilities(abilities: Collection<Ability>) {
        this.abilities.clear()
        this.archetypePoints.clear()
        this.height = 0
        abilities.forEach {
            this.abilities.add(it)
            it.getArchetype()?.let { archetype ->
                val point = this.archetypePoints.getOrDefault(archetype, 0)
                this.archetypePoints[archetype] = point + 1
            }
            this.height = max(this.height, it.getHeight())
        }
    }
}