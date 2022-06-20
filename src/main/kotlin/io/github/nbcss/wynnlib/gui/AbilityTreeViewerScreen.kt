package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.lang.Translations
import io.github.nbcss.wynnlib.utils.ItemFactory
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier

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
    }
    private var character: CharacterClass = CharacterClass.WARRIOR

    private fun drawCharacterTab(matrices: MatrixStack, index: Int, mouseX: Int, mouseY: Int) {
        val posX = windowX + 242
        val posY = windowY + 44 + index * 28
        val v = if (character.ordinal == index) 172 else 144
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

    override fun drawBackgroundPre(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPre(matrices, mouseX, mouseY, delta)
        (0 until CharacterClass.values().size)
            .filter { CharacterClass.values()[it] != character }
            .forEach { drawCharacterTab(matrices!!, it, mouseX, mouseY) }
    }

    override fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPost(matrices, mouseX, mouseY, delta)
        drawCharacterTab(matrices!!, character.ordinal, mouseX, mouseY)
        renderTexture(matrices, texture, windowX + 4, windowY + 42, 0, 0, 238, 144)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        CharacterClass.values()
            .firstOrNull {isOverCharacterTab(it.ordinal, mouseX.toInt(), mouseY.toInt())}?.let {
                this.character = it
                return true
            }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun drawContents(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        //todo
        val posX = windowX + 9
        val posY = windowY + 188
        //render archetype values
        val icon1 = ItemFactory.fromEncoding("minecraft:stone_axe#74")
        itemRenderer.renderInGuiWithOverrides(icon1, posX, posY)
        textRenderer.draw(matrices, "13", posX.toFloat() + 20, posY.toFloat() + 4, 0)
        val icon2 = ItemFactory.fromEncoding("minecraft:stone_axe#75")
        itemRenderer.renderInGuiWithOverrides(icon2, posX + 60, posY)
        textRenderer.draw(matrices, "14", posX.toFloat() + 80, posY.toFloat() + 4, 0)
        val icon3 = ItemFactory.fromEncoding("minecraft:stone_axe#76")
        itemRenderer.renderInGuiWithOverrides(icon3, posX + 120, posY)
        textRenderer.draw(matrices, "12", posX.toFloat() + 140, posY.toFloat() + 4, 0)
        //val icon4 = ItemFactory.fromEncoding("minecraft:stone_axe#83")
        //itemRenderer.renderInGuiWithOverrides(icon4, posX + 180, posY)
        //textRenderer.draw(matrices, "30/45", posX.toFloat() + 200, posY.toFloat() + 4, 0)
    }
}