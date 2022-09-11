package io.github.nbcss.wynnlib.gui.ability

import io.github.nbcss.wynnlib.abilities.builder.AbilityBuild
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.gui.DictionaryScreen
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.gui.TabFactory
import io.github.nbcss.wynnlib.gui.dicts.IngredientDictScreen
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.registry.AbilityBuildStorage
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

class AbilityBuildDictionaryScreen(parent: Screen?): DictionaryScreen<AbilityBuild>(parent, TITLE) {
    companion object {
        val ICON: ItemStack = ItemFactory.fromEncoding("minecraft:book")
        val TITLE: Text = Translations.UI_TREE_BUILDS.translate()
        /*val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(parent: Screen?): HandbookTabScreen = AbilityBuildDictionaryScreen(parent)
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is AbilityBuildDictionaryScreen
                    || screen is AbilityTreeViewerScreen
        }*/
    }

    override fun fetchItems(): Collection<AbilityBuild> {
        return AbilityBuildStorage.getAll()
    }

    override fun drawBackgroundPre(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPre(matrices, mouseX, mouseY, delta)
        (0 until CharacterClass.values().size)
            .forEach { drawCharacterTab(matrices!!, it, mouseX, mouseY) }
    }

    override fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPost(matrices, mouseX, mouseY, delta)
        drawDictionaryTab(matrices!!, mouseX, mouseY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        CharacterClass.values()
            .firstOrNull {isOverCharacterTab(it.ordinal, mouseX.toInt(), mouseY.toInt())}?.let {
                playSound(SoundEvents.ITEM_BOOK_PAGE_TURN)
                client!!.setScreen(AbilityTreeViewerScreen(parent, it))
                return true
            }
        return super.mouseClicked(mouseX, mouseY, button)
    }

    private fun drawDictionaryTab(matrices: MatrixStack, mouseX: Int, mouseY: Int) {
        val posX = windowX + 242
        val posY = windowY + 174
        val v = 210
        RenderKit.renderTexture(matrices, AbstractAbilityTreeScreen.TEXTURE, posX, posY, 0, v, 32, 28)
        itemRenderer.renderInGuiWithOverrides(ICON, posX + 7, posY + 6)
        if (isOverCharacterTab(CharacterClass.values().size, mouseX, mouseY)){
            drawTooltip(matrices, listOf(TITLE), mouseX, mouseY)
        }
    }

    private fun drawCharacterTab(matrices: MatrixStack, index: Int, mouseX: Int, mouseY: Int) {
        val posX = windowX + 242
        val posY = windowY + 34 + index * 28
        RenderKit.renderTexture(matrices, AbstractAbilityTreeScreen.TEXTURE, posX, posY,
            0, 182, 32, 28)
        val character = CharacterClass.values()[index]
        val icon = character.getWeapon().getIcon()
        itemRenderer.renderInGuiWithOverrides(icon, posX + 7, posY + 6)
        if (isOverCharacterTab(index, mouseX, mouseY)){
            drawTooltip(matrices, listOf(character.translate()), mouseX, mouseY)
        }
    }

    private fun isOverCharacterTab(index: Int, mouseX: Int, mouseY: Int): Boolean {
        val posX = windowX + 245
        val posY = windowY + 34 + index * 28
        return mouseX >= posX && mouseX < posX + 29 && mouseY >= posY && mouseY < posY + 28
    }
}