package io.github.nbcss.wynnlib.gui.ability

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.Archetype
import io.github.nbcss.wynnlib.utils.Pos
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.Formatting
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.min

class AbilityTreeBuilderScreen(parent: Screen?,
                               private val tree: AbilityTree): AbstractAbilityTreeScreen(parent) {
    companion object {
        const val MAX_AP = 45
    }
    private val abilities: MutableMap<Ability, NodeState> = HashMap()
    private val archetypePoints: MutableMap<Archetype, Int> = EnumMap(Archetype::class.java)
    private var ap: Int = MAX_AP
    init {
        tabs.clear()
        reset()
    }

    fun reset() {
        tree.getAbilities().forEach { abilities[it] = NodeState.LOCKED }
        tree.getRootAbility()?.let {
            abilities[it] = NodeState.UNLOCKED
        }
    }

    private fun canUnlock(ability: Ability): Boolean {
        if (ap < ability.getAbilityPointCost())
            return false
        if (tree.getArchetypes().any { ability.getArchetypeRequirement(it) > (archetypePoints[it] ?: 0) })
            return false
        return true
    }

    private fun update() {
        //reset current state
        archetypePoints.clear()
        ap = MAX_AP
        //validation
        val validated: MutableSet<Ability> = HashSet()
        val selected: Set<Ability> = abilities.filter { it.value == NodeState.ACTIVE }.map { it.key }.toSet()
        val queue: Queue<Ability> = LinkedList()
        tree.getRootAbility()?.let { queue.add(it) }
        while (queue.isNotEmpty()){
            val ability = queue.poll()

        }
        /*for (successor in ability.getSuccessors()) {
            if (abilities[successor] == NodeState.LOCKED){
                abilities[successor] = NodeState.UNLOCKED
            }
        }*/
    }

    override fun getAbilityTree(): AbilityTree = tree

    override fun onClickNode(ability: Ability, button: Int): Boolean {
        if (button == 0){
            val state = abilities[ability]
            if (state == NodeState.UNLOCKED){
                abilities[ability] = NodeState.ACTIVE
                update()
            }else if(state == NodeState.ACTIVE) {
                abilities[ability] = NodeState.UNLOCKED
                update()
            }
            return true
        }
        return super.onClickNode(ability, button)
    }

    override fun renderViewer(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        val list = tree.getAbilities().toList()
        renderEdges(list, matrices, LOCKED_INNER_COLOR, false)
        renderEdges(list, matrices, LOCKED_OUTER_COLOR, true)
        val nodes: MutableList<Pair<Pair<Pos, Pos>, Boolean>> = ArrayList()
        tree.getAbilities().forEach {
            val n = toScreenPosition(it.getHeight(), it.getPosition())
            it.getPredecessors().forEach { predecessor ->
                val m = toScreenPosition(predecessor.getHeight(), predecessor.getPosition())
                val height = min(it.getHeight(), predecessor.getHeight())
                val reroute = getAbilityTree().getAbilityFromPosition(height, it.getPosition()) != null
                if (abilities[it] == NodeState.ACTIVE && abilities[predecessor] == NodeState.ACTIVE){
                    nodes.add((m to n) to reroute)
                }
            }
        }
        nodes.forEach {
            drawOuterEdge(matrices, it.first.first, it.first.second, ACTIVE_OUTER_COLOR.getColorCode(), it.second)
        }
        nodes.forEach {
            drawInnerEdge(matrices, it.first.first, it.first.second, ACTIVE_INNER_COLOR.getColorCode(), it.second)
        }
        tree.getAbilities().forEach {
            val node = toScreenPosition(it.getHeight(), it.getPosition())
            renderArchetypeOutline(matrices, it, node.x, node.y)
            val item = when(abilities[it]){
                NodeState.ACTIVE -> it.getTier().getActiveTexture()
                NodeState.UNLOCKED -> if (isOverNode(node, mouseX, mouseY))
                    it.getTier().getActiveTexture() else it.getTier().getUnlockedTexture()
                NodeState.UNLOCKABLE -> if (isOverNode(node, mouseX, mouseY))
                    it.getTier().getActiveTexture() else it.getTier().getUnlockedTexture()
                else -> it.getTier().getLockedTexture()
            }
            itemRenderer.renderInGuiWithOverrides(item, node.x - 8, node.y - 8)
        }
    }

    override fun renderExtra(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        var archetypeX = viewerX + 2
        val archetypeY = viewerY + 143
        //render archetype values
        tree.getArchetypes().forEach {
            val icon = it.getTexture()
            val iconText = Formatting.BOLD.toString() + it.getIconText()
            val points = "${archetypePoints[it]?: 0}/${tree.getArchetypePoint(it)}"
            itemRenderer.renderInGuiWithOverrides(icon, archetypeX, archetypeY)
            itemRenderer.renderGuiItemOverlay(textRenderer, icon, archetypeX, archetypeY, iconText)
            textRenderer.draw(matrices, points, archetypeX.toFloat() + 20, archetypeY.toFloat() + 4, 0)
            archetypeX += 60
        }
        //render ap points
        run {
            val points = "$ap/$MAX_AP"
            itemRenderer.renderInGuiWithOverrides(ICON, archetypeX, archetypeY)
            textRenderer.draw(matrices, points, archetypeX.toFloat() + 18, archetypeY.toFloat() + 4, 0)
        }
        //render extra pane
        //todo
        //render ability tooltip
        if (isOverViewer(mouseX, mouseY)){
            for (ability in tree.getAbilities()) {
                val node = toScreenPosition(ability.getHeight(), ability.getPosition())
                if (isOverNode(node, mouseX, mouseY)){
                    drawTooltip(matrices, ability.getTooltip(), mouseX, mouseY + 20)
                    break
                }
            }
        }
    }
    enum class NodeState {
        ACTIVE,     //Already active nodes
        UNLOCKED,   //Already unlocked, can be active in one step
        UNLOCKABLE, //Possible to unlock if you go particular path
        LOCKED;     //Impossible to unlock in current setup
    }
}