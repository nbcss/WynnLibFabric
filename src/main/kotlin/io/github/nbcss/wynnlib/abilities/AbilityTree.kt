package io.github.nbcss.wynnlib.abilities

import io.github.nbcss.wynnlib.abilities.properties.BoundSpellProperty
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.data.SpellSlot
import io.github.nbcss.wynnlib.utils.Pos
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.max

class AbilityTree(val character: CharacterClass) {
    private val archetypes: List<Archetype> = Archetype.values().filter { it.getCharacter() == character }.toList()
    private val archetypePoints: MutableMap<Archetype, Int> = EnumMap(Archetype::class.java)
    private val posMap: MutableMap<Pos, Ability> = HashMap()
    private val abilities: MutableSet<Ability> = HashSet()
    private val spellMap: MutableMap<SpellSlot, Ability> = EnumMap(SpellSlot::class.java)
    private var height: Int = 0

    fun getArchetypes(): List<Archetype> = archetypes

    fun getArchetypePoint(archetype: Archetype): Int = archetypePoints.getOrDefault(archetype, 0)

    fun getAbilityFromPosition(height: Int, position: Int): Ability? {
        return posMap[Pos(height, position)]
    }

    fun getMaxHeight(): Int = height

    fun getAbilities(): Collection<Ability> {
        return abilities
    }

    fun getSpellAbility(spell: SpellSlot): Ability? {
        return spellMap[spell]
    }

    fun setAbilities(abilities: Collection<Ability>) {
        this.abilities.clear()
        this.archetypePoints.clear()
        this.posMap.clear()
        this.spellMap.clear()
        this.height = 0
        abilities.forEach {
            this.abilities.add(it)
            this.posMap[Pos(it.getHeight(), it.getPosition())] = it
            if(it.getTier().getLevel() == 0){
                it.getProperty(BoundSpellProperty.getKey())?.let { property ->
                    this.spellMap[(property as BoundSpellProperty).getSpell()] = it
                }
            }
            it.getArchetype()?.let { archetype ->
                val point = this.archetypePoints.getOrDefault(archetype, 0)
                this.archetypePoints[archetype] = point + 1
            }
            this.height = max(this.height, it.getHeight())
        }
    }
}