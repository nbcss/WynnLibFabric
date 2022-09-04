package io.github.nbcss.wynnlib.gui.ability

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.AbilityBuild
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.Archetype
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.gui.TabFactory
import io.github.nbcss.wynnlib.gui.widgets.ATreeScrollWidget
import io.github.nbcss.wynnlib.gui.widgets.RollingTextWidget
import io.github.nbcss.wynnlib.gui.widgets.VerticalSliderWidget
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_LOCKED
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_UNUSABLE
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.render.RenderKit.renderOutlineText
import io.github.nbcss.wynnlib.render.TextureData
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.IntPos
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.playSound
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

open class AbilityTreeBuilderScreen(parent: Screen?,
                                    private val tree: AbilityTree,
                                    private val maxPoints: Int = MAX_AP,
                                    private val fixedAbilities: Set<Ability> =
                                   tree.getMainAttackAbility()?.let { setOf(it) } ?: emptySet()):
    AbstractAbilityTreeScreen(parent), AbilityBuild {
    companion object {
        private val OVERVIEW_PANE = Identifier("wynnlib", "textures/gui/ability_overview.png")
        private val SLIDER_TEXTURE = TextureData(OVERVIEW_PANE, 148, 0)
        private const val SLIDER_LENGTH = 40
        const val MAX_AP = 45
        const val MAX_ENTRY_ITEM = 8
    }
    private val activeNodes: MutableSet<Ability> = HashSet()
    private val orderList: MutableList<Ability> = mutableListOf()
    private val paths: MutableMap<Ability, List<Ability>> = HashMap()
    private val archetypePoints: MutableMap<Archetype, Int> = EnumMap(Archetype::class.java)
    private var container: EntryContainer = EntryContainer()
    private var ap: Int = maxPoints
    private var entryIndex = 0
    private var overviewSlider: VerticalSliderWidget? = null
    private val entryNames: Array<RollingTextWidget?> = arrayOfNulls(MAX_ENTRY_ITEM)
    private val entryValues: Array<RollingTextWidget?> = arrayOfNulls(MAX_ENTRY_ITEM)
    private var viewer: BuilderWindow? = null
    init {
        tabs.clear()
        tabs.add(object : TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(parent: Screen?): HandbookTabScreen = copy()
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is AbilityTreeBuilderScreen
        })
        reset()
    }

    fun getFixedAbilities(): Set<Ability> = fixedAbilities

    open fun copy(): AbilityTreeBuilderScreen {
        return AbilityTreeBuilderScreen(parent, tree)
    }

    fun reset() {
        activeNodes.clear()
        activeNodes.addAll(fixedAbilities)
        ap = maxPoints
        for (ability in fixedAbilities) {
            ap -= ability.getAbilityPointCost()
            ability.getArchetype()?.let {
                archetypePoints[it] = 1 + (archetypePoints[it] ?: 0)
            }
        }
        update()
    }

    fun getActivateOrders(): List<Ability> = orderList

    fun getMaxPoints(): Int = maxPoints

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

    private fun fixNodes() {
        //reset current state
        orderList.clear()
        archetypePoints.clear()
        ap = maxPoints
        //put fixed abilities first
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
                    if (ability !in fixedAbilities) {
                        orderList.add(ability)
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
        activeNodes.addAll(fixedAbilities)
    }

    private fun update() {
        paths.clear()
        tree.getAbilities().forEach { paths[it] = ArrayList() }
        //compute path
        //todo replace with optimal search
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
        //fixme test it out...
        //AbilityPath.compute(tree, activeNodes, archetypePoints)
        /*tree.getAbilities().lastOrNull()?.let {
            AbilityPath.test(tree, activeNodes, archetypePoints, it)
        }*/
        //update container
        container = EntryContainer(activeNodes)
        setEntryIndex(entryIndex) //for update entry
        updateEntrySlider()
    }

    private fun setEntryIndex(index: Int) {
        val maxIndex = max(0, container.getSize() - MAX_ENTRY_ITEM)
        entryIndex = MathHelper.clamp(index, 0, maxIndex)
        for (i in (0 until MAX_ENTRY_ITEM)){
            val entry = if (i + entryIndex < container.getSize()) container.getEntries()[i + entryIndex] else null
            entryNames[i]?.setText(entry?.getDisplayNameText())
            entryValues[i]?.setText(entry?.getSideText())
        }
    }

    private fun updateEntrySlider() {
        overviewSlider?.setSlider(if (container.getSize() > MAX_ENTRY_ITEM) {
            entryIndex / (container.getSize() - MAX_ENTRY_ITEM).toDouble()
        }else{
            0.0
        })
    }

    private fun isInEntries(mouseX: Double, mouseY: Double): Boolean {
        val x1 = windowX - 142
        val x2 = windowX - 4
        val y1 = windowY + 44
        val y2 = windowY + 204
        return mouseX >= x1 && mouseX < x2 && mouseY >= y1 && mouseY < y2
    }

    override fun getViewer(): ATreeScrollWidget? = viewer

    override fun getAbilityTree(): AbilityTree = tree

    override fun getTitle(): Text {
        return title.copy().append(" [$ap/$maxPoints]")
    }

    override fun init() {
        super.init()
        windowX = 148 + (width - windowWidth - 148) / 2
        viewerX = windowX + 7
        exitButton!!.x = windowX + 230
        viewer = BuilderWindow(viewerX, viewerY, viewerX + 223, viewerY)
        overviewSlider = VerticalSliderWidget(windowX - 17, windowY + 45,
            12, 158, SLIDER_LENGTH, SLIDER_TEXTURE) {
            val size = container.getSize() - MAX_ENTRY_ITEM + 1
            if (size > 0) {
                setEntryIndex(floor(size * it).toInt())
            }
        }
        updateEntrySlider()
        for (i in (0 until MAX_ENTRY_ITEM)){
            val entry = if (i + entryIndex < container.getSize()) container.getEntries()[i + entryIndex] else null
            val posX = windowX - 118
            val posY = windowY + 46 + i * 20
            val name = entry?.getDisplayNameText()
            val info = entry?.getSideText()
            entryNames[i] = RollingTextWidget(posX, posY, 95, name)
            entryValues[i] = RollingTextWidget(posX, posY + 9, 95, info)
        }
    }

    override fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPost(matrices, mouseX, mouseY, delta)
        RenderKit.renderTexture(matrices, OVERVIEW_PANE, windowX - 147,
            windowY + 28, 0, 0, 148, 182)
        textRenderer.draw(
            matrices, Translations.TOOLTIP_ABILITY_OVERVIEW.translate(),
            (windowX - 147 + 6).toFloat(),
            (windowY + 34).toFloat(), Color.WHITE.code()
        )
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (overviewSlider?.mouseReleased(mouseX, mouseY, button) == true){
            return true
        }
        return super.mouseReleased(mouseX, mouseY, button)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (overviewSlider?.mouseDragged(mouseX, mouseY, button, 0.0, 0.0) == true) {
            return true
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (isInEntries(mouseX, mouseY)) {
            if (overviewSlider?.mouseClicked(mouseX, mouseY, button) == true){
                return true
            }
            val entries = container.getEntries()
            for (i in (0 until min(MAX_ENTRY_ITEM, entries.size))){
                val entry = entries[entryIndex + i]
                val x1 = windowX - 141
                val y1 = windowY + 45 + i * 20
                val y2 = y1 + 18
                if (mouseY >= y1 - 1 && mouseY <= y2 && mouseX >= x1 - 1 && mouseX < x1 + 122){
                    playSound(SoundEvents.ENTITY_ITEM_PICKUP)
                    getViewer()?.focusOnAbility(entry.getAbility())
                    return true
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        //println("${mouseX}, ${mouseY}, $amount")
        if(overviewSlider?.isDragging() != true && isInEntries(mouseX, mouseY)){
            setEntryIndex(entryIndex - amount.toInt())
            updateEntrySlider()
            return true
        }
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun renderExtra(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        var archetypeX = viewerX + 2
        val archetypeY = viewerY + 144
        //render archetype values
        tree.getArchetypes().forEach {
            renderArchetypeIcon(matrices, it, archetypeX, archetypeY)
            val points = "${archetypePoints[it]?: 0}/${tree.getArchetypePoint(it)}"
            textRenderer.draw(matrices, points, archetypeX.toFloat() + 20, archetypeY.toFloat() + 4, 0)
            if (mouseX >= archetypeX && mouseY >= archetypeY && mouseX <= archetypeX + 16 && mouseY <= archetypeY + 16){
                drawTooltip(matrices, it.getTooltip(this), mouseX, mouseY)
            }
            archetypeX += 60
        }
        //render ap points
        run {
            itemRenderer.renderInGuiWithOverrides(ICON, archetypeX, archetypeY)
            textRenderer.draw(matrices, "$ap/$maxPoints",
                archetypeX.toFloat() + 18, archetypeY.toFloat() + 4, 0)
        }
        //Render overview pane
        val entries = container.getEntries()
        overviewSlider?.visible = entries.size > MAX_ENTRY_ITEM
        overviewSlider?.render(matrices, mouseX, mouseY, delta)
        for (i in (0 until min(MAX_ENTRY_ITEM, entries.size))){
            val entry = entries[entryIndex + i]
            val x1 = windowX - 141
            val x2 = x1 + 18
            val y1 = windowY + 45 + i * 20
            val y2 = y1 + 18
            RenderSystem.enableDepthTest()
            RenderKit.renderTexture(matrices, entry.getTexture(), x1, y1, 0, 0,
                18, 18, 18, 18)
            val tier = entry.getTierText()
            renderOutlineText(matrices, tier,
                x2.toFloat() - textRenderer.getWidth(tier) + 1,
                y2.toFloat() - 7)
            entryNames[i]?.render(matrices)
            entryValues[i]?.render(matrices)
            if (mouseY >= y1 - 1 && mouseY <= y2 && mouseX >= x1 - 1 && mouseX < x1 + 122){
                DrawableHelper.fill(matrices, x1 - 1, y1 - 1, x1 + 122, y2 + 1,
                    Color.WHITE.withAlpha(0x22).code())
                matrices.push()
                drawTooltip(matrices, entry.getTooltip(), mouseX, mouseY)
                matrices.pop()
            }
        }
    }

    override fun getSpareAbilityPoints(): Int = ap

    override fun getArchetypePoint(archetype: Archetype): Int = archetypePoints[archetype] ?: 0

    override fun hasAbility(ability: Ability): Boolean = ability in activeNodes

    inner class BuilderWindow(x: Int, y: Int, sliderX: Int, sliderY: Int) :
        ATreeScrollWidget(this@AbilityTreeBuilderScreen, x, y, sliderX, sliderY) {
        override fun getAbilityTree(): AbilityTree = tree

        override fun renderContents(matrices: MatrixStack, mouseX: Int, mouseY: Int, position: Double, delta: Float) {
            val list = tree.getAbilities().toList()
            renderEdges(list, matrices, LOCKED_INNER_COLOR, false)
            renderEdges(list, matrices, LOCKED_OUTER_COLOR, true)
            val nodes: MutableList<Pair<Pair<IntPos, IntPos>, Boolean>> = ArrayList()
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
                drawOuterEdge(matrices, it.first.first, it.first.second, ACTIVE_OUTER_COLOR.code(), it.second)
            }
            nodes.forEach {
                drawInnerEdge(matrices, it.first.first, it.first.second, ACTIVE_INNER_COLOR.code(), it.second)
            }
            tree.getAbilities().forEach {
                val node = toScreenPosition(it.getHeight(), it.getPosition())
                renderArchetypeOutline(matrices, it, node.x, node.y)
                val path = paths[it]
                val icon = if (it in activeNodes){
                    it.getTier().getActiveTexture()
                }else if (path == null || path.isEmpty()){
                    it.getTier().getLockedTexture()
                }else if(isMouseOver(mouseX.toDouble(), mouseY.toDouble()) && isOverNode(node, mouseX, mouseY)){
                    it.getTier().getActiveTexture()     //hover over unlockable node
                }else{
                    it.getTier().getUnlockedTexture()
                }
                itemRenderer.renderInGuiWithOverrides(icon, node.x - 8, node.y - 8)
                if (container.isAbilityDisabled(it)) {
                    matrices.push()
                    matrices.translate(0.0, 0.0, 200.0)
                    renderOutlineText(matrices, Symbol.WARNING.asText(), node.x.toFloat() + 4, node.y.toFloat() + 2)
                    matrices.pop()
                }
            }
        }

        override fun onClickNode(ability: Ability, button: Int): Boolean {
            if (button == 0){
                if (ability in fixedAbilities){
                    playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED)
                    return true
                }
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
                            fixNodes()
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

        override fun renderContentsPost(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
            if (!isMouseOver(mouseX.toDouble(), mouseY.toDouble()))
                return
            //render ability tooltip
            for (ability in tree.getAbilities()) {
                val node = toScreenPosition(ability.getHeight(), ability.getPosition())
                if (isOverNode(node, mouseX, mouseY)){
                    val tooltip = ability.getTooltip(this@AbilityTreeBuilderScreen).toMutableList()
                    val disabled = container.isAbilityDisabled(ability)
                    val locked = ability in fixedAbilities
                    if (disabled || locked) {
                        tooltip.add(LiteralText.EMPTY)
                        if (locked) {
                            tooltip.add(
                                Symbol.WARNING.asText().append(" ")
                                    .append(TOOLTIP_ABILITY_LOCKED.formatted(Formatting.RED)))
                        }
                        if(disabled) {
                            tooltip.add(
                                Symbol.WARNING.asText().append(" ")
                                    .append(TOOLTIP_ABILITY_UNUSABLE.formatted(Formatting.RED)))
                        }
                    }
                    //drawTooltip(matrices, tooltip, mouseX, mouseY + 20)
                    renderAbilityTooltip(matrices, mouseX, mouseY, ability, tooltip)
                    break
                }
            }
        }
    }
}