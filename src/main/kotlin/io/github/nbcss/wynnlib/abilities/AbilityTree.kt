package io.github.nbcss.wynnlib.abilities

import io.github.nbcss.wynnlib.abilities.properties.info.BoundSpellProperty
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
    private val costMap: MutableMap<Pair<Ability, Ability>, Int> = mutableMapOf()
    private val posMap: MutableMap<Pos, Ability> = HashMap()
    private val abilities: MutableSet<Ability> = HashSet()
    private val spellMap: MutableMap<SpellSlot, Ability> = EnumMap(SpellSlot::class.java)
    private var mainAttack: Ability? = null
    private var root: Ability? = null
    private var height: Int = 0

    fun getRootAbility(): Ability? = root

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

    fun getMainAttackAbility(): Ability? {
        return mainAttack
    }

    /**
     * Get total (minimum) cost from given ability x (assume active) to another ability y.
     * The cost will NOT consider the requirements/dependency.
     * If the node x cannot lead to node y, the method will return null.
     *
     * @param from source node x, assuming it is active (it's the cost will not count)
     * @param to the destination node y.
     * @return minimum cost from x to y, or null if x cannot arrive y.
     */
    fun getMinimumCost(from: Ability, to: Ability): Int? {
        return costMap[from to to]
    }

    fun setAbilities(abilities: Collection<Ability>) {
        this.abilities.clear()
        this.archetypePoints.clear()
        this.posMap.clear()
        this.spellMap.clear()
        this.height = 0
        this.mainAttack = null
        this.root = null
        abilities.forEach {
            this.abilities.add(it)
            this.posMap[Pos(it.getHeight(), it.getPosition())] = it
            if (it.isMainAttack()){
                mainAttack = it
            }
            if(it.getHeight() == 0){
                root = it
            }
            if(it.getTier().getLevel() == 0){
                BoundSpellProperty.from(it)?.let { property ->
                    this.spellMap[property.getSpell()] = it
                }
            }
            it.getArchetype()?.let { archetype ->
                val point = this.archetypePoints.getOrDefault(archetype, 0)
                this.archetypePoints[archetype] = point + 1
            }
            this.height = max(this.height, it.getHeight())
        }
        this.costMap.clear()
        this.abilities.forEach { from ->
            costMap[from to from] = 0
            val queue: Queue<Pair<Ability, Int>> = PriorityQueue(compareBy{ it.second })
            queue.add(from to from.getAbilityPointCost())
            while (queue.isNotEmpty()) {
                val pair = queue.remove()
                val node = pair.first
                for (successor in node.getSuccessors()) {
                    val nextCost = successor.getAbilityPointCost() + costMap[from to node]!!
                    if (costMap[from to successor] == null || nextCost < costMap[from to successor]!!){
                        costMap[from to successor] = nextCost
                        queue.add(successor to nextCost)
                    }
                }
            }
        }
        //edges(this)
    }
}