package io.github.nbcss.wynnlib.gui.ability

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.gui.TabFactory
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.playSound
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Identifier


class AbilityTreeViewerScreen(parent: Screen?) : AbstractAbilityTreeScreen(parent) {
    private val texture = Identifier("wynnlib", "textures/gui/ability_tree_viewer.png")
    companion object {
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(parent: Screen?): HandbookTabScreen = AbilityTreeViewerScreen(parent)
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is AbilityTreeViewerScreen
        }
    }
    private var tree: AbilityTree = AbilityRegistry.fromCharacter(CharacterClass.WARRIOR)

    private fun drawCharacterTab(matrices: MatrixStack, index: Int, mouseX: Int, mouseY: Int) {
        val posX = windowX + 242
        val posY = windowY + 44 + index * 28
        val v = if (tree.character.ordinal == index) 172 else 144
        RenderKit.renderTexture(matrices, texture, posX, posY, 0, v, 32, 28)
        val character = CharacterClass.values()[index]
        val icon = character.getWeapon().getIcon()
        itemRenderer.renderInGuiWithOverrides(icon, posX + 7, posY + 6)
        if (isOverCharacterTab(index, mouseX, mouseY)){
            drawTooltip(matrices, listOf(character.translate()), mouseX, mouseY)
        }
    }

    private fun isOverCharacterTab(index: Int, mouseX: Int, mouseY: Int): Boolean {
        val posX = windowX + 245
        val posY = windowY + 44 + index * 28
        return mouseX >= posX && mouseX < posX + 29 && mouseY >= posY && mouseY < posY + 28
    }

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
        (0 until CharacterClass.values().size)
            .filter { CharacterClass.values()[it] != tree.character }
            .forEach { drawCharacterTab(matrices!!, it, mouseX, mouseY) }
    }

    override fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPost(matrices, mouseX, mouseY, delta)
        drawCharacterTab(matrices!!, tree.character.ordinal, mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        CharacterClass.values()
            .firstOrNull {isOverCharacterTab(it.ordinal, mouseX.toInt(), mouseY.toInt())}?.let {
                this.tree = AbilityRegistry.fromCharacter(it)
                playSound(SoundEvents.ITEM_BOOK_PAGE_TURN)
                resetScroll()
                return true
            }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun onClickNode(ability: Ability, button: Int): Boolean {
        return super.onClickNode(ability, 1)
    }

    override fun renderViewer(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        //render node background
        //render inactive edges (basic)
        val inactive = tree.getAbilities().filter {
            !isOverViewer(mouseX, mouseY) || !isOverNode(
                toScreenPosition(it.getHeight(), it.getPosition()), mouseX, mouseY)
        }
        renderEdges(inactive, matrices, LOCKED_OUTER_COLOR, false)
        renderEdges(inactive, matrices, LOCKED_INNER_COLOR, true)
        //render active edges
        val active = tree.getAbilities().filter {
            isOverViewer(mouseX, mouseY) && isOverNode(toScreenPosition(it.getHeight(), it.getPosition()), mouseX, mouseY)
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
            }else if (isOverViewer(mouseX, mouseY) && isOverNode(node, mouseX, mouseY)){
                it.getTier().getActiveTexture()
            }else{
                it.getTier().getUnlockedTexture()
            }
            itemRenderer.renderInGuiWithOverrides(item, node.x - 8, node.y - 8)
        }
    }

    override fun renderExtra(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        var archetypeX = viewerX + 2
        val archetypeY = viewerY + 143
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
        //render ability tooltip
        if (isOverViewer(mouseX, mouseY)){
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