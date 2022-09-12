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
import io.github.nbcss.wynnlib.gui.widgets.SquareButton
import io.github.nbcss.wynnlib.gui.widgets.VerticalSliderWidget
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_LOCKED
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_UNUSABLE
import io.github.nbcss.wynnlib.registry.AbilityBuildStorage
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.render.RenderKit.renderOutlineText
import io.github.nbcss.wynnlib.render.TextureData
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.IntPos
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.playSound
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.ButtonWidget
import net.minecraft.client.gui.widget.TextFieldWidget
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
                                    tree.getMainAttackAbility()?.let { setOf(it) } ?: emptySet(),
                                    private val mutableAbilities: Set<Ability> = emptySet(),
                                    private var build: AbilityBuild = AbilityBuild(tree, maxPoints,
                                        fixedAbilities.union(mutableAbilities))):
    AbstractAbilityTreeScreen(parent) {
    companion object {
        private val OVERVIEW_PANE = Identifier("wynnlib", "textures/gui/ability_overview.png")
        private val BUILD_NAME_PANE = Identifier("wynnlib", "textures/gui/tree_build_panel.png")
        private val SAVE_BUTTON = Identifier("wynnlib", "textures/gui/save_button.png")
        private val DELETE_BUTTON = Identifier("wynnlib", "textures/gui/delete_button.png")
        private val SLIDER_TEXTURE = TextureData(OVERVIEW_PANE, 148, 0)
        private const val SLIDER_LENGTH = 40
        const val MAX_AP = 45
        const val MAX_ENTRY_ITEM = 8
    }
    private var container: EntryContainer = EntryContainer()
    private var entryIndex = 0
    private var overviewSlider: VerticalSliderWidget? = null
    private val entryNames: Array<RollingTextWidget?> = arrayOfNulls(MAX_ENTRY_ITEM)
    private val entryValues: Array<RollingTextWidget?> = arrayOfNulls(MAX_ENTRY_ITEM)
    private var viewer: BuilderWindow? = null
    private var saveButton: SquareButton? = null
    private var deleteButton: SquareButton? = null
    private var nameField: TextFieldWidget? = null
    init {
        tabs.clear()
        tabs.add(object : TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(parent: Screen?): HandbookTabScreen = copy()
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is AbilityTreeBuilderScreen
        })
        update()
    }

    fun getFixedAbilities(): Set<Ability> = fixedAbilities

    fun getMutableAbilities(): Set<Ability> = mutableAbilities

    open fun copy(): AbilityTreeBuilderScreen {
        build.clear()
        return AbilityTreeBuilderScreen(parent, tree, build = build)
    }

    fun getRemovedAbilities(): Set<Ability> = mutableAbilities.subtract(build.getActiveAbilities())

    fun getActivateOrders(): List<Ability> = build.getActivateOrders()
        .filter { it !in fixedAbilities && it !in mutableAbilities }

    fun getMaxPoints(): Int = maxPoints

    private fun update() {
        //update container
        container = EntryContainer(build.getActiveAbilities())
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
        return title.copy().append(" [${build.getSpareAbilityPoints()}/$maxPoints]")
    }

    override fun tick() {
        super.tick()
        nameField?.tick()
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
        nameField = TextFieldWidget(textRenderer, windowX - 115, windowY + 14, 105, 12, LiteralText.EMPTY)
        nameField?.text = build.getCustomName()
        nameField?.setDrawsBackground(false)
        nameField?.setChangedListener {
            build.setName(it)
        }
        addDrawableChild(nameField)
        saveButton = SquareButton(SAVE_BUTTON, windowX - 15, windowY + 1, 10) {
            AbilityBuildStorage.put(build)
            it.visible = false
        }
        saveButton?.visible = !AbilityBuildStorage.has(build.getKey())
        addDrawableChild(saveButton)
        deleteButton = SquareButton(DELETE_BUTTON, windowX - 15, windowY + 1, 10) {
            AbilityBuildStorage.remove(build.getKey())
            it.visible = false
        }
        deleteButton?.visible = AbilityBuildStorage.has(build.getKey())
        addDrawableChild(deleteButton)
    }

    override fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPost(matrices, mouseX, mouseY, delta)
        RenderKit.renderTexture(matrices, OVERVIEW_PANE, windowX - 147,
            windowY + 28, 0, 0, 148, 182)
        RenderKit.renderTexture(matrices, BUILD_NAME_PANE, windowX - 147,
            windowY - 2, 0, 0, 148, 30, 148, 30)
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

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (nameField?.isFocused == true && this.client!!.options.inventoryKey.matchesKey(keyCode, scanCode)){
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
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
        //Render name widgets
        val hasBuild = AbilityBuildStorage.has(build.getKey())
        saveButton?.visible = !hasBuild
        deleteButton?.visible = hasBuild
        build.getMainArchetype()?.let {
            renderArchetypeIcon(matrices, it, windowX - 138, windowY + 5)
        }
        nameField?.let {
            if (!it.isFocused && it.text.isEmpty()) {
                val s = textRenderer.trimToWidth(build.getEncoding(), 106) + ".."
                textRenderer.draw(matrices, LiteralText(s).formatted(Formatting.ITALIC).formatted(Formatting.DARK_GRAY),
                    windowX - 115.0f, windowY + 14.0f, 0xFFFFFF)
            }
        }
        //Archetypes
        var archetypeX = viewerX + 2
        val archetypeY = viewerY + 144
        //render archetype values
        tree.getArchetypes().forEach {
            renderArchetypeIcon(matrices, it, archetypeX, archetypeY)
            val points = "${build.getArchetypePoint(it)}/${tree.getArchetypePoint(it)}"
            textRenderer.draw(matrices, points, archetypeX.toFloat() + 20, archetypeY.toFloat() + 4, 0)
            if (mouseX >= archetypeX && mouseY >= archetypeY && mouseX <= archetypeX + 16 && mouseY <= archetypeY + 16){
                drawTooltip(matrices, it.getTooltip(build), mouseX, mouseY)
            }
            archetypeX += 60
        }
        //render ap points
        run {
            itemRenderer.renderInGuiWithOverrides(ICON, archetypeX, archetypeY)
            textRenderer.draw(matrices, "${build.getSpareAbilityPoints()}/$maxPoints",
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
                    if (build.hasAbility(it) && build.hasAbility(predecessor)){
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
                val icon = if (build.hasAbility(it)){
                    it.getTier().getActiveTexture()
                }else if (!build.isUnlockable(it)){
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
                if (build.hasAbility(ability)){
                    playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL, 0.5f)
                    build.removeAbility(ability)
                    update()
                }else if(build.addAbility(ability)){
                    playSound(SoundEvents.BLOCK_END_PORTAL_FRAME_FILL)
                    update()
                }else{
                    playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED)
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
                    val tooltip = ability.getTooltip(build).toMutableList()
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