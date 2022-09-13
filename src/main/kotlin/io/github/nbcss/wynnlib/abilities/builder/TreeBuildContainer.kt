package io.github.nbcss.wynnlib.abilities.builder

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.Archetype
import java.util.*

class TreeBuildContainer(private val data: TreeBuildData,
                         private val maxPoints: Int = MAX_AP): TreeBuildInfo {
    companion object {
        const val MAX_AP = 45
        fun fromAbilities(
            tree: AbilityTree,
            abilities: Set<Ability> = emptySet(),
            maxPoints: Int = MAX_AP
        ): TreeBuildContainer {
            val data = TreeBuildData(tree)
            data.setAbilities(abilities)
            return TreeBuildContainer(data, maxPoints)
        }

        fun fromBuild(data: TreeBuildData, maxPoints: Int = MAX_AP): TreeBuildContainer {
            return TreeBuildContainer(data, maxPoints)
        }
    }
    private val orderList: MutableList<Ability> = mutableListOf()
    private val unlockable: MutableSet<Ability> = mutableSetOf()
    init {
        validateAbilities()
    }

    fun getData(): TreeBuildData = data

    fun getActiveOrder(): List<Ability> = orderList

    fun setAbilities(abilities: Set<Ability>) {
        data.setAbilities(abilities)
        validateAbilities()
    }

    fun addAbility(ability: Ability): Boolean {
        if (isUnlockable(ability)) {
            data.addAbility(ability)
            orderList.add(ability)
            updateUnlockable()
            return true
        }
        return false
    }

    fun removeAbility(ability: Ability) {
        if (data.hasAbility(ability)) {
            data.removeAbility(ability)
            validateAbilities()
        }
    }

    fun isUnlockable(ability: Ability): Boolean = ability in unlockable

    private fun validateAbilities() {
        val state = stateOf(data.getActiveAbilities())
        data.setAbilities(state.abilities)
        orderList.clear()
        orderList.addAll(state.order)
        updateUnlockable(state)
    }

    private fun canUnlock(ability: Ability, state: TreeState): Boolean {
        if (state.cost + ability.getAbilityPointCost() > maxPoints)
            return false
        if (data.getTree().getArchetypes().any { ability.getArchetypeRequirement(it) > state.getArchetype(it) })
            return false
        if (ability.getBlockAbilities().any { state.contains(it) })
            return false
        val dependency = ability.getAbilityDependency()
        if (dependency != null && !state.contains(dependency))
            return false
        return true
    }

    private fun stateOf(abilities: Set<Ability>): TreeState {
        //validation
        val state = TreeState()
        val queue: PriorityQueue<Ability> = PriorityQueue(compareBy { it.getPage() })
        data.getTree().getRootAbility()?.let { queue.add(it) }
        val locked: MutableSet<Ability> = mutableSetOf()
        var lastAttempt: Set<Ability>
        do {
            lastAttempt = locked.toSet()
            locked.clear()
            while (queue.isNotEmpty()) {
                val ability = queue.poll()
                if (ability !in abilities || state.contains(ability)){
                    continue    //if not active by user, it must stay inactive
                }
                //check whether eligible to activating the node
                if (canUnlock(ability, state)){
                    state.add(ability)
                    ability.getSuccessors().filter { it in abilities }.forEach { queue.add(it) }
                }else{
                    locked.add(ability)
                }
            }
            queue.addAll(locked)
        } while (lastAttempt != locked)
        /*//replace active nodes with all validated nodes
        build.setAbilities(state.abilities)
        orderList.clear()
        orderList.addAll(state.order)
        updateUnlockable(state)*/
        return state
    }

    private fun getCurrentState(): TreeState {
        return TreeState(data.getTotalCost(), orderList,
            data.getActiveAbilities().toMutableSet(),
            mutableMapOf(pairs = data.getTree().getArchetypes()
                .map { it to data.getArchetypePoint(it) }
                .toTypedArray())
        )
    }

    private fun updateUnlockable(state: TreeState = getCurrentState()) {
        //update unlockable nodes
        unlockable.clear()
        for (ability in data.getActiveAbilities()) {
            for (successor in ability.getSuccessors()) {
                if (successor !in state.abilities && canUnlock(successor, state)){
                    unlockable.add(successor)
                }
            }
        }
        data.getTree().getRootAbility()?.let {
            if (it !in state.abilities) unlockable.add(it)
        }
    }

    private data class TreeState(var cost: Int = 0,
                                 val order: MutableList<Ability> = mutableListOf(),
                                 val abilities: MutableSet<Ability> = mutableSetOf(),
                                 val archetypes: MutableMap<Archetype, Int> = EnumMap(Archetype::class.java)) {

        fun add(ability: Ability) {
            if (abilities.add(ability)) {
                cost += ability.getAbilityPointCost()
                ability.getArchetype()?.let {
                    archetypes[it] = 1 + (archetypes[it] ?: 0)
                }
                order.add(ability)
            }
        }

        fun getArchetype(archetype: Archetype): Int {
            return archetypes[archetype] ?: 0
        }

        fun contains(ability: Ability): Boolean {
            return ability in abilities
        }
    }

    override fun getAbilities(): Set<Ability> {
        return data.getActiveAbilities()
    }

    override fun hasAbility(ability: Ability): Boolean {
        return data.hasAbility(ability)
    }

    override fun getSpareAbilityPoints(): Int {
        return maxPoints - data.getTotalCost()
    }

    override fun getArchetypePoint(archetype: Archetype): Int {
        return data.getArchetypePoint(archetype)
    }
}