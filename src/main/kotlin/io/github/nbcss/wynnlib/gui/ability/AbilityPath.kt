package io.github.nbcss.wynnlib.gui.ability

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.Archetype
import java.util.*

class AbilityPath(private val tree: AbilityTree,
                  private val parent: AbilityPath?,
                  private val active: Set<Ability>,
                  private val blocks: Set<Ability>,
                  private val archetypes: Map<Archetype, Int>,
                  private val action: Ability?,
                  private val cost: Int): Comparable<AbilityPath> {
    companion object {
        fun compute(tree: AbilityTree/*,
                    nodes: Set<Ability>,
                    archetypes: Map<Archetype, Int>*/) {
            /*val blocks = nodes.map { it.getBlockAbilities() }.flatten().toSet()
            val root = AbilityPath(tree, null, nodes, blocks, archetypes, null, 0)
            val output: MutableSet<Ability> = mutableSetOf()
            val queue: Queue<AbilityPath> = PriorityQueue()
            queue.add(root)
            while (queue.isNotEmpty()) {
                val node = queue.remove()
                println("dequeue: " + node.action + " | " + node.getActions())
                for (successor in node.getSuccessors()) {
                    val next = node.next(successor)
                    if (successor !in output){
                        //output result
                        println("$successor: " + next.getActions())
                        output.add(successor)
                        queue.add(next)
                    }else if (successor.getArchetype() != null) {
                        queue.add(next)
                    }
                }
            }*/
        }

        fun test(tree: AbilityTree,
                 nodes: Set<Ability>,
                 archetypes: Map<Archetype, Int>,
                 destination: Ability) {
            println("dest $destination")
            val blocks = nodes.map { it.getBlockAbilities() }.flatten().toSet()
            val root = AbilityPath(tree, null, nodes, blocks, archetypes, null, 0)
            val costMap: MutableMap<Ability, Int> = mutableMapOf()
            val heuristic: MutableMap<Ability, Int> = mutableMapOf()
            for (ability in tree.getAbilities()) {
                heuristic[ability] = Int.MAX_VALUE
            }
            for (ability in nodes) {
                heuristic[ability] = tree.getMinimumCost(ability, destination) ?: Int.MAX_VALUE
            }
            val queue: Queue<AbilityPath> = PriorityQueue(compareBy {
                heuristic[it.action]
            })
            queue.add(root)
            while (queue.isNotEmpty()) {
                val node = queue.remove()
                println("dequeue ${node.action} ${node.cost}")
                if (node.action == destination){
                    println(node.getActions())
                    break
                }
                for (successor in node.getSuccessors()) {
                    val nextCost = node.cost + successor.getAbilityPointCost()
                    println(" - $successor ($nextCost)")
                    val next = node.next(successor)
                    val expectCost = next.active.mapNotNull { tree.getMinimumCost(it, destination) }.minOrNull()
                    if (expectCost != null) {
                        heuristic[successor] = nextCost + expectCost
                        println("enqueue $successor")
                        queue.add(next)
                    }else{
                        println("$successor -> $destination")
                        println("uhhh what?")
                    }
                }
            }
        }
    }

    private fun canUnlock(ability: Ability): Boolean {
        if (tree.getArchetypes().any { ability.getArchetypeRequirement(it) > (archetypes[it] ?: 0) })
            return false
        if (ability in blocks)
            return false
        val dependency = ability.getAbilityDependency()
        if (dependency != null && dependency !in active)
            return false
        return true
    }

    fun getActions(): MutableList<Ability> {
        val actions = parent?.getActions() ?: mutableListOf()
        action?.let { actions.add(it) }
        return actions
    }

    fun getSuccessors(): Collection<Ability> {
        val nodes: MutableSet<Ability> = mutableSetOf()
        for (ability in active) {
            for (successor in ability.getSuccessors()) {
                if (successor !in nodes && successor !in active && canUnlock(successor)) {
                    nodes.add(successor)
                }
            }
        }
        //root node always unlockable (if not already unlocked)
        tree.getRootAbility()?.let {
            if (it !in active) nodes.add(it)
        }
        return nodes
    }

    fun next(action: Ability): AbilityPath {
        val nodes = active.toMutableSet()
        nodes.add(action)
        val archetypeMap = if (action.getArchetype() == null){
            archetypes
        }else{
            val map = archetypes.toMutableMap()
            map[action.getArchetype()!!] = archetypes[action.getArchetype()] ?: 0
            map
        }
        val blockAbilities = action.getBlockAbilities()
        val blockList = if (blockAbilities.isEmpty()){
            blocks
        }else{
            val set = blocks.toMutableSet()
            set.addAll(blockAbilities)
            set
        }
        return AbilityPath(tree, this, nodes,
            blockList,
            archetypeMap,
            action,
            cost + action.getAbilityPointCost())
    }

    override fun compareTo(other: AbilityPath): Int {
        return cost.compareTo(other.cost)
    }
}