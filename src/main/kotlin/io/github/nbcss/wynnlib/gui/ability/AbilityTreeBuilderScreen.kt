package io.github.nbcss.wynnlib.gui.ability

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.AbilityBuild
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.Archetype
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.Pos
import io.github.nbcss.wynnlib.utils.playSound
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.min

class AbilityTreeBuilderScreen(parent: Screen?,
                               private val tree: AbilityTree):
    AbstractAbilityTreeScreen(parent), AbilityBuild {
    companion object {
        const val MAX_AP = 45
        const val PANE_WIDTH = 118
    }
    private val activeNodes: MutableSet<Ability> = HashSet()
    private val paths: MutableMap<Ability, List<Ability>> = HashMap()
    private val archetypePoints: MutableMap<Archetype, Int> = EnumMap(Archetype::class.java)
    private var container: EntryContainer = EntryContainer()
    private var ap: Int = MAX_AP
    init {
        tabs.clear()
        reset()
    }

    fun reset() {
        activeNodes.clear()
        update()
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

    private fun fixNodes () {
        //reset current state
        archetypePoints.clear()
        ap = MAX_AP
        //validation
        val validated: MutableSet<Ability> = HashSet()
        val queue: Queue<Ability> = LinkedList()
        tree.getRootAbility()?.let { queue.add(it) }
        var lastSkip: MutableSet<Ability> = HashSet()
        while (true) {
            queue.addAll(lastSkip)
            val skipped: MutableSet<Ability> = HashSet()
            while (queue.isNotEmpty()){
                val ability = queue.poll()
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
                    ability.getSuccessors().forEach { queue.add(it) }
                }else{
                    skipped.add(ability)
                }
            }
            if(skipped == lastSkip)
                break
            lastSkip = skipped
        }
        //replace active nodes with all validated nodes
        activeNodes.clear()
        activeNodes.addAll(validated)
    }

    private fun update() {
        paths.clear()
        tree.getAbilities().forEach { paths[it] = ArrayList() }
        //compute path
        //todo replace with optimal search
        if(activeNodes.isEmpty()){
            tree.getRootAbility()?.let { paths[it] = listOf(it) }
        }else{
            for (ability in activeNodes) {
                for (successor in ability.getSuccessors()) {
                    if (canUnlock(successor, activeNodes)){
                        paths[successor] = listOf(successor)
                    }
                }
            }
        }
        //update container
        container = EntryContainer(activeNodes)
    }

    override fun getAbilityTree(): AbilityTree = tree

    override fun getTitle(): Text {
        return title.copy().append(" [$ap/$MAX_AP]")
    }

    override fun init() {
        super.init()
        windowX = PANE_WIDTH + (width - windowWidth - PANE_WIDTH) / 2
        viewerX = windowX + 7
        exitButton!!.x = windowX + 230
    }

    override fun onClickNode(ability: Ability, button: Int): Boolean {
        if (button == 0){
            if (ability in activeNodes){
                playSound(SoundEvents.BLOCK_LAVA_POP)
                activeNodes.remove(ability)
                fixNodes()
                update()
            }else{
                paths[ability]?.let {
                    if (it.isNotEmpty()){
                        //Successful Add
                        playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL)
                        for (node in it) {
                            activeNodes.add(node)
                            ap -= ability.getAbilityPointCost()
                            ability.getArchetype()?.let { arch ->
                                archetypePoints[arch] = 1 + (archetypePoints[arch] ?: 0)
                            }
                        }
                        update()
                    }else{
                        playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED)
                    }
                }
            }
            return true
        }
        return super.onClickNode(ability, button)
    }

    override fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPost(matrices, mouseX, mouseY, delta)
        val pane = Identifier("wynnlib", "textures/gui/extend_pane.png")
        RenderKit.renderTexture(matrices, pane, windowX - PANE_WIDTH, windowY + 28, 0, 0, PANE_WIDTH, 210)
        textRenderer.draw(
            matrices, LiteralText("Ability List"),
            (windowX - PANE_WIDTH + 6).toFloat(),
            (windowY + 34).toFloat(), 0
        )
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
                if (it in activeNodes && predecessor in activeNodes){
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
            val path = paths[it]
            val icon = if (it in activeNodes){
                it.getTier().getActiveTexture()
            }else if (path == null || path.isEmpty()){
                it.getTier().getLockedTexture()
            }else if(isOverNode(node, mouseX, mouseY)){
                it.getTier().getActiveTexture()     //hover over unlockable node
            }else{
                it.getTier().getUnlockedTexture()
            }
            itemRenderer.renderInGuiWithOverrides(icon, node.x - 8, node.y - 8)
        }
    }

    override fun renderExtra(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        //render extra pane content
        container.getEntries().forEachIndexed { i, entry ->
            val x1 = windowX - PANE_WIDTH + 6
            val x2 = x1 + 18
            val y1 = windowY + 44 + i * 20
            val y2 = y1 + 18
            RenderSystem.enableDepthTest()
            DrawableHelper.fill(matrices, x1 - 1, y1 - 1, x1 + 106, y2 + 1, Color.DARK_GRAY.toSolidColor().getColorCode())
            RenderKit.renderTexture(matrices, entry.getTexture(), x1, y1, 0, 0,
                18, 18, 18, 18)
            val tier = entry.getTierText()
            textRenderer.drawWithShadow(matrices, tier,
                x2.toFloat() - textRenderer.getWidth(tier) + 1,
                y2.toFloat() - 7, 0xFFFFFF
            )
            textRenderer.drawWithShadow(matrices, entry.getDisplayNameText(),
                x2.toFloat() + 3,
                y1.toFloat() + 1, 0
            )
            textRenderer.drawWithShadow(matrices, entry.getSideText(),
                x2.toFloat() + 3,
                y1.toFloat() + 10, 0xFFFFFF
            )
            if (mouseY in y1..y2 && mouseX >= x1 && mouseX <= x1 + 100){
                drawTooltip(matrices, entry.getTooltip(), mouseX, mouseY)
            }
        }
        //========****=========
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
            if (mouseX >= archetypeX && mouseY >= archetypeY && mouseX <= archetypeX + 16 && mouseY <= archetypeY + 16){
                drawTooltip(matrices, it.getTooltip(this), mouseX, mouseY)
            }
            archetypeX += 60
        }
        //render ap points
        run {
            itemRenderer.renderInGuiWithOverrides(ICON, archetypeX, archetypeY)
            textRenderer.draw(matrices, "$ap/$MAX_AP", archetypeX.toFloat() + 18, archetypeY.toFloat() + 4, 0)
        }
        //render ability tooltip
        if (isOverViewer(mouseX, mouseY)){
            for (ability in tree.getAbilities()) {
                val node = toScreenPosition(ability.getHeight(), ability.getPosition())
                if (isOverNode(node, mouseX, mouseY)){
                    drawTooltip(matrices, ability.getTooltip(this), mouseX, mouseY + 20)
                    break
                }
            }
        }
    }

    override fun getSpareAbilityPoints(): Int = ap

    override fun getArchetypePoint(archetype: Archetype): Int = archetypePoints[archetype] ?: 0

    override fun hasAbility(ability: Ability): Boolean = ability in activeNodes
}