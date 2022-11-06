package io.github.nbcss.wynnlib.gui.ability

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.gui.TabFactory
import io.github.nbcss.wynnlib.gui.widgets.ATreeScrollWidget
import io.github.nbcss.wynnlib.gui.widgets.buttons.*
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.ItemFactory
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting


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
            override fun shouldDisplay(): Boolean = true
        }
    }
    private val buttons: MutableList<SideTabWidget> = mutableListOf()
    private var tree: AbilityTree = AbilityRegistry.fromCharacter(character)
    private var viewer: ViewerWindow? = null

    override fun init() {
        super.init()
        viewer = ViewerWindow(viewerX, viewerY, viewerX + 223, viewerY)
        buttons.clear()
        var index = 0
        for (characterClass in CharacterClass.values()) {
            val handler = object : SideTabWidget.Handler {
                override fun onClick(index: Int) {
                    tree = AbilityRegistry.fromCharacter(characterClass)
                    getViewer()?.reset()
                }
                override fun isSelected(index: Int): Boolean {
                    return tree.character.ordinal == index
                }
                override fun drawTooltip(matrices: MatrixStack, mouseX: Int, mouseY: Int, index: Int) {
                    drawTooltip(matrices, listOf(characterClass.translate()), mouseX, mouseY)
                }
            }
            buttons.add(SideTabWidget.fromWindowSide(index++, windowX, windowY, 34,
                SideTabWidget.Side.LEFT, characterClass.getWeapon().getIcon(), handler))
        }
        buttons.add(SideTabWidget.fromWindowSide(index, windowX, windowY, 34,
            SideTabWidget.Side.LEFT, AbilityBuildDictionaryScreen.ICON, object : SideTabWidget.Handler {
                override fun onClick(index: Int) {
                    val screen = AbilityBuildDictionaryScreen(parent)
                    client!!.setScreen(screen)
                }
                override fun isSelected(index: Int): Boolean = false
                override fun drawTooltip(matrices: MatrixStack, mouseX: Int, mouseY: Int, index: Int) {
                    drawTooltip(matrices, listOf(AbilityBuildDictionaryScreen.TITLE), mouseX, mouseY)
                }
            }))
        buttons.add(SideTabWidget.fromWindowSide(0, windowX, windowY, 45,
            SideTabWidget.Side.RIGHT, CREATE_ICON, object : SideTabWidget.Handler {
                override fun onClick(index: Int) {
                    client!!.setScreen(AbilityTreeBuilderScreen(this@AbilityTreeViewerScreen, tree))
                }
                override fun isSelected(index: Int): Boolean = false
                override fun getClickSound(): SoundEvent? = SoundEvents.ENTITY_ITEM_PICKUP
                override fun drawTooltip(matrices: MatrixStack, mouseX: Int, mouseY: Int, index: Int) {
                    val name = Translations.UI_TREE_BUILDS.translate().string
                    drawTooltip(matrices, listOf(LiteralText("[+] $name").formatted(Formatting.GREEN),
                        tree.character.formatted(Formatting.GRAY)), mouseX, mouseY)
                }
            }))
    }

    override fun getViewer(): ATreeScrollWidget? = viewer

    override fun getAbilityTree(): AbilityTree = tree

    override fun getTitle(): Text {
        return title.copy().append(" [").append(tree.character.translate()).append("]")
    }

    override fun drawBackgroundPre(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPre(matrices, mouseX, mouseY, delta)
        buttons.forEach { it.drawBackgroundPre(matrices, mouseX, mouseY) }
    }

    override fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPost(matrices, mouseX, mouseY, delta)
        buttons.forEach { it.drawBackgroundPost(matrices, mouseX, mouseY) }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (buttons.any { it.mouseClicked(mouseX.toInt(), mouseY.toInt(), button) })
            return true
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

        override fun renderContents(
            matrices: MatrixStack,
            mouseX: Int,
            mouseY: Int,
            position: Double,
            delta: Float,
            mouseOver: Boolean
        ) {
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