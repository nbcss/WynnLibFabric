package io.github.nbcss.wynnlib.gui.ability

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.gui.TabFactory
import io.github.nbcss.wynnlib.gui.widgets.ATreeScrollWidget
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.playSound
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.LiteralText
import net.minecraft.text.Text


class AbilityTreeViewerScreen(parent: Screen?,
                              character: CharacterClass = CharacterClass.values()[0]) : AbstractAbilityTreeScreen(parent) {
    companion object {
        val CREATE_ICON = ItemFactory.fromEncoding("minecraft:writable_book")
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(parent: Screen?): HandbookTabScreen = AbilityTreeViewerScreen(parent)
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is AbilityBuildDictionaryScreen
                    || screen is AbilityTreeViewerScreen
        }
    }
    private var tree: AbilityTree = AbilityRegistry.fromCharacter(character)
    private var viewer: ViewerWindow? = null

    private fun drawDictionaryTab(matrices: MatrixStack, mouseX: Int, mouseY: Int) {
        val posX = windowX + 242
        val posY = windowY + 174
        val v = 182
        RenderKit.renderTexture(matrices, TEXTURE, posX, posY, 0, v, 32, 28)
        itemRenderer.renderInGuiWithOverrides(AbilityBuildDictionaryScreen.ICON, posX + 7, posY + 6)
        if (isOverCharacterTab(CharacterClass.values().size, mouseX, mouseY)){
            drawTooltip(matrices, listOf(AbilityBuildDictionaryScreen.TITLE), mouseX, mouseY)
        }
    }

    private fun drawCharacterTab(matrices: MatrixStack, index: Int, mouseX: Int, mouseY: Int) {
        val posX = windowX + 242
        val posY = windowY + 34 + index * 28
        val v = if (tree.character.ordinal == index) 210 else 182
        RenderKit.renderTexture(matrices, TEXTURE, posX, posY, 0, v, 32, 28)
        val character = CharacterClass.values()[index]
        val icon = character.getWeapon().getIcon()
        itemRenderer.renderInGuiWithOverrides(icon, posX + 7, posY + 6)
        if (isOverCharacterTab(index, mouseX, mouseY)){
            drawTooltip(matrices, listOf(character.translate()), mouseX, mouseY)
        }
    }

    private fun drawCreateTreeTab(matrices: MatrixStack, mouseX: Int, mouseY: Int) {
        val posX = windowX - 32
        val posY = windowY + 50
        RenderKit.renderTexture(matrices, TEXTURE, posX, posY, 0, 182, 32, 28)
        itemRenderer.renderInGuiWithOverrides(CREATE_ICON, posX + 7, posY + 6)
        if (isOverCreateTreeTab(mouseX, mouseY)){
            drawTooltip(matrices, listOf(LiteralText("Add")), mouseX, mouseY)
        }
    }

    private fun isOverCreateTreeTab(mouseX: Int, mouseY: Int): Boolean {
        val posX = windowX - 32
        val posY = windowY + 50
        return mouseX >= posX && mouseX < posX + 29 && mouseY >= posY && mouseY < posY + 28
    }

    private fun isOverCharacterTab(index: Int, mouseX: Int, mouseY: Int): Boolean {
        val posX = windowX + 245
        val posY = windowY + 34 + index * 28
        return mouseX >= posX && mouseX < posX + 29 && mouseY >= posY && mouseY < posY + 28
    }

    override fun init() {
        super.init()
        viewer = ViewerWindow(viewerX, viewerY, viewerX + 223, viewerY)
    }

    override fun getViewer(): ATreeScrollWidget? = viewer

    override fun getAbilityTree(): AbilityTree = tree

    override fun getTitle(): Text {
        return title.copy().append(" [").append(tree.character.translate()).append("]")
    }

    override fun keyPressed(keyCode: Int, scanCode: Int, modifiers: Int): Boolean {
        if (keyCode == 32){
            client!!.setScreen(AbilityTreeBuilderScreen(this, tree))
            return true
        }
        return super.keyPressed(keyCode, scanCode, modifiers)
    }

    override fun drawBackgroundPre(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPre(matrices, mouseX, mouseY, delta)
        drawDictionaryTab(matrices!!, mouseX, mouseY)
        drawCreateTreeTab(matrices, mouseX, mouseY)
        (0 until CharacterClass.values().size)
            .filter { CharacterClass.values()[it] != tree.character }
            .forEach { drawCharacterTab(matrices, it, mouseX, mouseY) }
    }

    override fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPost(matrices, mouseX, mouseY, delta)
        drawCharacterTab(matrices!!, tree.character.ordinal, mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (button == 0){
            if (isOverCharacterTab(CharacterClass.values().size, mouseX.toInt(), mouseY.toInt())) {
                val screen = AbilityBuildDictionaryScreen(parent)
                client!!.setScreen(screen)
                playSound(SoundEvents.ITEM_BOOK_PAGE_TURN)
                return true
            }
            if (isOverCreateTreeTab(mouseX.toInt(), mouseY.toInt())) {
                client!!.setScreen(AbilityTreeBuilderScreen(this, tree))
                playSound(SoundEvents.ITEM_LODESTONE_COMPASS_LOCK)
                return true
            }
            CharacterClass.values()
                .firstOrNull {isOverCharacterTab(it.ordinal, mouseX.toInt(), mouseY.toInt())}?.let {
                    this.tree = AbilityRegistry.fromCharacter(it)
                    playSound(SoundEvents.ITEM_BOOK_PAGE_TURN)
                    getViewer()?.reset()
                    return true
                }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun renderExtra(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        var archetypeX = viewerX + 2
        val archetypeY = viewerY + 144
        //render archetype values
        tree.getArchetypes().forEach {
            renderArchetypeIcon(matrices, it, archetypeX, archetypeY)
            val points = tree.getArchetypePoint(it).toString()
            textRenderer.draw(matrices, points, archetypeX.toFloat() + 20, archetypeY.toFloat() + 4, 0)
            if (mouseX >= archetypeX && mouseY >= archetypeY && mouseX <= archetypeX + 16 && mouseY <= archetypeY + 16){
                drawTooltip(matrices, it.getTooltip(), mouseX, mouseY)
            }
            archetypeX += 60
        }
    }

    inner class ViewerWindow(x: Int, y: Int, sliderX: Int, sliderY: Int) :
        ATreeScrollWidget(this@AbilityTreeViewerScreen, x, y, sliderX, sliderY) {
        override fun getAbilityTree(): AbilityTree = tree

        override fun renderContents(matrices: MatrixStack, mouseX: Int, mouseY: Int, position: Double, delta: Float) {
            //render node background
            //render inactive edges (basic)
            val inactive = tree.getAbilities().filter {
                !isMouseOver(mouseX.toDouble(), mouseY.toDouble()) || !isOverNode(
                    toScreenPosition(it.getHeight(), it.getPosition()), mouseX, mouseY)
            }
            renderEdges(inactive, matrices, LOCKED_OUTER_COLOR, false)
            renderEdges(inactive, matrices, LOCKED_INNER_COLOR, true)
            //render active edges
            val active = tree.getAbilities().filter {
                isMouseOver(mouseX.toDouble(), mouseY.toDouble()) &&
                        isOverNode(toScreenPosition(it.getHeight(), it.getPosition()), mouseX, mouseY)
            }
            renderEdges(active, matrices, ACTIVE_OUTER_COLOR, false)
            renderEdges(active, matrices, ACTIVE_INNER_COLOR, true)
            //render icons
            val locked = HashSet(active.map { it.getBlockAbilities() }.flatten())
            tree.getAbilities().forEach {
                val node = toScreenPosition(it.getHeight(), it.getPosition())
                renderArchetypeOutline(matrices, it, node.x, node.y)
                val item = if (it in locked){
                    it.getTier().getLockedTexture()
                }else if (isMouseOver(mouseX.toDouble(), mouseY.toDouble()) && isOverNode(node, mouseX, mouseY)){
                    it.getTier().getActiveTexture()
                }else{
                    it.getTier().getUnlockedTexture()
                }
                itemRenderer.renderInGuiWithOverrides(item, node.x - 8, node.y - 8)
            }
        }

        override fun onClickNode(ability: Ability, button: Int): Boolean {
            return super.onClickNode(ability, 1)
        }

        override fun renderContentsPost(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
            if (!isMouseOver(mouseX.toDouble(), mouseY.toDouble()))
                return
            //render ability tooltip
            for (ability in tree.getAbilities()) {
                val node = toScreenPosition(ability.getHeight(), ability.getPosition())
                if (isOverNode(node, mouseX, mouseY)){
                    renderAbilityTooltip(matrices, mouseX, mouseY, ability)
                    break
                }
            }
        }
    }
}