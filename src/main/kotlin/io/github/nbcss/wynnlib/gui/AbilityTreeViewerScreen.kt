package io.github.nbcss.wynnlib.gui

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.lang.Translations
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.Pos
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import kotlin.math.abs
import kotlin.math.max

class AbilityTreeViewerScreen(parent: Screen?) : HandbookTabScreen(parent, TITLE) {
    private val texture = Identifier("wynnlib", "textures/gui/ability_tree_viewer.png")
    companion object {
        val ICON: ItemStack = ItemFactory.fromEncoding("minecraft:stone_axe#83")
        val TITLE: Text = Translations.UI_ABILITY_TREE.translate()
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(parent: Screen?): HandbookTabScreen = AbilityTreeViewerScreen(parent)
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is AbilityTreeViewerScreen
        }
        const val GRID_SIZE: Int = 24
        const val VIEW_WIDTH = 232
        const val VIEW_HEIGHT = 138
        const val SCROLL_FRAMES = 4
        const val ACTIVE_OUTER_COLOR: Int = 0x37ACB5 + (0xFF shl 24)
        const val ACTIVE_INNER_COLOR: Int = 0x5FD6DF + (0xFF shl 24)
        const val LOCKED_OUTER_COLOR: Int = 0x2D2E30 + (0xFF shl 24)
        const val LOCKED_INNER_COLOR: Int = 0x252527 + (0xFF shl 24)
        const val BASIC_OUTER_COLOR: Int = 0xEEEEEE + (0xFF shl 24)
        const val BASIC_INNER_COLOR: Int = 0xAAAAAA + (0xFF shl 24)
    }
    private var tree: AbilityTree = AbilityRegistry.fromCharacter(CharacterClass.WARRIOR)
    private var viewerX: Int = 0
    private var viewerY: Int = 0
    private var scroll: Int = 0
    private var movingScroll: Int = 0
    private var scrollTicks: Int = 0

    private fun drawOuterEdge(matrices: MatrixStack, from: Pos, to: Pos, color: Int){
        RenderSystem.enableDepthTest()
        if (from.x != to.x){
            DrawableHelper.fill(matrices, from.x, from.y - 2, to.x, from.y + 2, color)
        }
        if (from.y != to.y){
            val offsetY = if (from.x == to.x) 0 else -2
            DrawableHelper.fill(matrices, to.x - 2, from.y + offsetY, to.x + 2, to.y, color)
        }
    }
    private fun drawInnerEdge(matrices: MatrixStack, from: Pos, to: Pos, color: Int){
        if (from.x != to.x){
            DrawableHelper.fill(matrices, from.x, from.y - 1, to.x, from.y + 1, color)
        }
        if (from.y != to.y){
            val offsetY = if (from.x == to.x) 0 else -1
            DrawableHelper.fill(matrices, to.x - 1, from.y + offsetY, to.x + 1, to.y, color)
        }
    }

    private fun toScreenPosition(height: Int, position: Int): Pos {
        val posX = windowX + 9 + GRID_SIZE / 2 + (position + 4) * GRID_SIZE
        val posY = windowY + 47 + GRID_SIZE / 2 + height * GRID_SIZE - movingScroll
        return Pos(posX, posY)
    }

    private fun drawCharacterTab(matrices: MatrixStack, index: Int, mouseX: Int, mouseY: Int) {
        val posX = windowX + 242
        val posY = windowY + 44 + index * 28
        val v = if (tree.character.ordinal == index) 172 else 144
        renderTexture(matrices, texture, posX, posY, 0, v, 32, 28)
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

    private fun isOverViewer(mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= viewerX && mouseX < viewerX + VIEW_WIDTH && mouseY >= viewerY && mouseY < viewerY + VIEW_HEIGHT
    }

    override fun init() {
        super.init()
        viewerX = windowX + 7
        viewerY = windowY + 45
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if (isOverViewer(mouseX.toInt(), mouseY.toInt())){
            val max = max(0, 4 + (1 + tree.getMaxHeight()) * GRID_SIZE - VIEW_HEIGHT)
            scroll = MathHelper.clamp(scroll - amount.toInt() * GRID_SIZE, 0, max)
            scrollTicks = SCROLL_FRAMES
        }
        return super.mouseScrolled(mouseX, mouseY, amount)
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
        renderTexture(matrices, texture, windowX + 4, windowY + 42, 0, 0, 238, 144)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        CharacterClass.values()
            .firstOrNull {isOverCharacterTab(it.ordinal, mouseX.toInt(), mouseY.toInt())}?.let {
                this.tree = AbilityRegistry.fromCharacter(it)
                this.scroll = 0
                this.movingScroll = 0
                this.scrollTicks = 0
                return true
            }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun drawContents(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        /*val posX = viewerX + 2
        val posY = viewerY + 143
        //render archetype values
        val icon1 = ItemFactory.fromEncoding("minecraft:stone_axe#74")
        itemRenderer.renderInGuiWithOverrides(icon1, posX, posY)
        textRenderer.draw(matrices, "13", posX.toFloat() + 20, posY.toFloat() + 4, 0)
        val icon2 = ItemFactory.fromEncoding("minecraft:stone_axe#75")
        itemRenderer.renderInGuiWithOverrides(icon2, posX + 60, posY)
        textRenderer.draw(matrices, "14", posX.toFloat() + 80, posY.toFloat() + 4, 0)
        val icon3 = ItemFactory.fromEncoding("minecraft:stone_axe#76")
        itemRenderer.renderInGuiWithOverrides(icon3, posX + 120, posY)
        textRenderer.draw(matrices, "12", posX.toFloat() + 140, posY.toFloat() + 4, 0)*/
        //val icon4 = ItemFactory.fromEncoding("minecraft:stone_axe#83")
        //itemRenderer.renderInGuiWithOverrides(icon4, posX + 180, posY)
        //textRenderer.draw(matrices, "30/45", posX.toFloat() + 200, posY.toFloat() + 4, 0)
        //update moving scroll
        if (scrollTicks > 0){
            movingScroll += ((scroll - movingScroll).toFloat() / scrollTicks.toFloat()).toInt()
            scrollTicks -= 1
        }else {
            movingScroll = scroll
        }
        val bottom = (viewerY + VIEW_HEIGHT)
        val scale = client!!.window.scaleFactor
        RenderSystem.enableScissor((viewerX * scale).toInt(),
            client!!.window.height - (bottom * scale).toInt(),
            (VIEW_WIDTH * scale).toInt(), (VIEW_HEIGHT * scale).toInt())
        //Render outer lines
        tree.getAbilities().forEach {
            val to = toScreenPosition(it.getHeight(), it.getPosition())
            it.getPredecessors().mapNotNull { x -> AbilityRegistry.get(x) }
                .forEach { node ->
                    val from = toScreenPosition(node.getHeight(), node.getPosition())
                    drawOuterEdge(matrices!!, from, to, ACTIVE_OUTER_COLOR)
                }
        }
        //render inner lines
        tree.getAbilities().forEach {
            val to = toScreenPosition(it.getHeight(), it.getPosition())
            it.getPredecessors().mapNotNull { x -> AbilityRegistry.get(x) }
                .forEach { node ->
                    val from = toScreenPosition(node.getHeight(), node.getPosition())
                    drawInnerEdge(matrices!!, from, to, ACTIVE_INNER_COLOR)
                }
        }
        //render icons
        tree.getAbilities().forEach {
            val node = toScreenPosition(it.getHeight(), it.getPosition())
            val texture = it.getTier().getUnlockedTexture()
            itemRenderer.renderInGuiWithOverrides(texture, node.x - 8, node.y - 8)
        }
        RenderSystem.disableScissor()
        //render ability tooltip
        if (isOverViewer(mouseX, mouseY)){
            for (ability in tree.getAbilities()) {
                val node = toScreenPosition(ability.getHeight(), ability.getPosition())
                if (abs(node.x - mouseX) <= 11 && abs(node.y - mouseY) <= 11){
                    drawTooltip(matrices!!, ability.getTooltip(), mouseX, mouseY)
                    break
                }
            }
        }
    }
}