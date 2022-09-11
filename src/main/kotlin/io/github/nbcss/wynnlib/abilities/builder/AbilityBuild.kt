package io.github.nbcss.wynnlib.abilities.builder

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.Archetype
import java.util.*

class AbilityBuild(val tree: AbilityTree,
                   val maxPoints: Int) {
    private val paths: MutableMap<Ability, List<Ability>> = mutableMapOf()
    private val activeNodes: MutableSet<Ability> = mutableSetOf()
    private val archetypePoints: MutableMap<Archetype, Int> = EnumMap(Archetype::class.java)
    private val orderList: MutableList<Ability> = mutableListOf()
    private var ap: Int = maxPoints

    fun getActivateOrders(): List<Ability> = orderList

    fun removeAbility(ability: Ability) {
        activeNodes.remove(ability)
        fixNodes()
        updatePaths()
    }

    fun addAbility(ability: Ability): Boolean {
        paths[ability]?.let {
            if (it.isEmpty())
                return false
            for (node in it) {
                activeNodes.add(node)
                ap -= ability.getAbilityPointCost()
                ability.getArchetype()?.let { arch ->
                    archetypePoints[arch] = 1 + (archetypePoints[arch] ?: 0)
                }
            }
            fixNodes()
            updatePaths()
            return true
        }
        return false
    }

    fun isUnlockable(ability: Ability): Boolean {
        val path = paths[ability]
        return path != null && path.isNotEmpty()
    }

    fun setAbilities(abilities: Set<Ability>) {
        activeNodes.clear()
        activeNodes.addAll(abilities.filter { !it.isMainAttack() })
        ap = maxPoints
        for (ability in activeNodes) {
            ap -= ability.getAbilityPointCost()
            ability.getArchetype()?.let {
                archetypePoints[it] = 1 + (archetypePoints[it] ?: 0)
            }
        }
        updatePaths()
    }

    private fun fixNodes() {
        //reset current state
        orderList.clear()
        archetypePoints.clear()
        ap = maxPoints
        //put fixed abilities first
        //validation
        val validated: MutableSet<Ability> = HashSet()
        var queue: MutableList<Ability> = mutableListOf()
        tree.getRootAbility()?.let { queue.add(it) }
        var lastSkip: MutableSet<Ability> = HashSet()
        while (true) {
            queue.addAll(lastSkip)
            val skipped: MutableSet<Ability> = HashSet()
            val nextQueue: Queue<Ability> = LinkedList()
            for (ability in queue) {
                if (ability !in activeNodes || ability in validated){
                    continue    //if not active by user, it must stay inactive
                }
                //check whether eligible to activating the node
                if (canUnlock(ability, validated)){
                    validated.add(ability)
                    ap -= ability.getAbilityPointCost()
                    ability.getArchetype()?.let {
                        archetypePoints[it] = 1 + (archetypePoints[it] ?: 0)
                    }
                    orderList.add(ability)
                    ability.getSuccessors().forEach { nextQueue.add(it) }
                }else{
                    skipped.add(ability)
                }
            }
            if (nextQueue.isNotEmpty()) {
                queue = nextQueue.sortedBy { it.getPage() }.toMutableList()
                continue
            }
            if(skipped == lastSkip)
                break
            lastSkip = skipped
        }
        //replace active nodes with all validated nodes
        activeNodes.clear()
        activeNodes.addAll(validated)
    }

    private fun canUnlock(ability: Ability, nodes: Collection<Ability>): Boolean {
        if (ap < ability.getAbilityPointCost())
            return false
        if (tree.getArchetypes().any { ability.getArchetypeRequirement(it) > (archetypePoints[it] ?: 0) })
            return false
        if (ability.getBlockAbilities().any { it in nodes })
            return false
        val dependency = ability.getAbilityDependency()
        if (dependency != null && dependency !in nodes)
            return false
        return true
    }

    private fun updatePaths() {
        paths.clear()
        tree.getAbilities().forEach { paths[it] = mutableListOf() }
        //compute path
        for (ability in activeNodes) {
            for (successor in ability.getSuccessors()) {
                if (canUnlock(successor, activeNodes)){
                    paths[successor] = listOf(successor)
                }
            }
        }
        tree.getRootAbility()?.let {
            if (it !in activeNodes) paths[it] = listOf(it)
        }
    }

    fun getActiveAbilities(): Set<Ability> = tree.getMainAttackAbility()?.let {
        activeNodes.union(setOf(it))
    } ?: activeNodes

    fun getSpareAbilityPoints(): Int = ap

    fun getArchetypePoint(archetype: Archetype): Int = archetypePoints[archetype] ?: 0

    fun hasAbility(ability: Ability): Boolean = ability in activeNodes || ability.isMainAttack()
}