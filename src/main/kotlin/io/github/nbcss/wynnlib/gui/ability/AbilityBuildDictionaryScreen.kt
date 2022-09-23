package io.github.nbcss.wynnlib.gui.ability

import io.github.nbcss.wynnlib.abilities.builder.TreeBuildData
import io.github.nbcss.wynnlib.abilities.builder.TreeBuildContainer
import io.github.nbcss.wynnlib.data.CharacterClass
import io.github.nbcss.wynnlib.gui.DictionaryScreen
import io.github.nbcss.wynnlib.gui.widgets.SideTabWidget
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.UI_CLIPBOARD_IMPORT
import io.github.nbcss.wynnlib.registry.AbilityBuildStorage
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.playSound
import io.github.nbcss.wynnlib.utils.readClipboard
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

open class AbilityBuildDictionaryScreen(parent: Screen?): DictionaryScreen<TreeBuildData>(parent, TITLE) {
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
    protected val buttons: MutableList<SideTabWidget> = mutableListOf()

    override fun init() {
        super.init()
        buttons.clear()
        var index = 0
        for (characterClass in CharacterClass.values()) {
            val handler = object : SideTabWidget.Handler {
                override fun onClick(index: Int) {
                    client!!.setScreen(AbilityTreeViewerScreen(parent, characterClass))
                }
                override fun isSelected(index: Int): Boolean = false
                override fun drawTooltip(matrices: MatrixStack, mouseX: Int, mouseY: Int, index: Int) {
                    drawTooltip(matrices, listOf(characterClass.translate()), mouseX, mouseY)
                }
            }
            buttons.add(SideTabWidget.fromWindowSide(index++, windowX, windowY, 34,
                SideTabWidget.Side.LEFT, characterClass.getWeapon().getIcon(), handler))
        }
        buttons.add(SideTabWidget.fromWindowSide(index, windowX, windowY, 34,
            SideTabWidget.Side.LEFT, ICON, object : SideTabWidget.Handler {
                override fun onClick(index: Int) {
                    val screen = AbilityBuildDictionaryScreen(parent)
                    client!!.setScreen(screen)
                }
                override fun isSelected(index: Int): Boolean = true
                override fun drawTooltip(matrices: MatrixStack, mouseX: Int, mouseY: Int, index: Int) {
                    drawTooltip(matrices, listOf(TITLE), mouseX, mouseY)
                }
            }))
        buttons.add(SideTabWidget.fromWindowSide(0, windowX, windowY, 45,
            SideTabWidget.Side.RIGHT, AbilityTreeViewerScreen.CREATE_ICON, object : SideTabWidget.Handler {
                override fun onClick(index: Int) {
                    val clipboard = readClipboard()
                    if (clipboard != null) {
                        val build = TreeBuildData.fromEncoding(clipboard)
                        if (build != null) {
                            client!!.setScreen(AbilityTreeBuilderScreen(
                                this@AbilityBuildDictionaryScreen, build.getTree(),
                                build = TreeBuildContainer.fromBuild(build)))
                            playSound(SoundEvents.ENTITY_ITEM_PICKUP)
                            return
                        }
                    }
                    playSound(SoundEvents.ENTITY_VILLAGER_NO)
                }
                override fun isSelected(index: Int): Boolean = false
                override fun getClickSound(): SoundEvent? = null
                override fun drawTooltip(matrices: MatrixStack, mouseX: Int, mouseY: Int, index: Int) {
                    val name = Translations.UI_TREE_BUILDS.translate().string
                    drawTooltip(matrices, listOf(LiteralText("[+] $name").formatted(Formatting.GREEN),
                        UI_CLIPBOARD_IMPORT.formatted(Formatting.GRAY)), mouseX, mouseY)
                }
            }))
    }

    override fun onClickItem(item: TreeBuildData, button: Int) {
        playSound(SoundEvents.ENTITY_ITEM_PICKUP)
        val screen = AbilityTreeBuilderScreen(this, item.getTree(),
            build = TreeBuildContainer.fromBuild(item))
        client!!.setScreen(screen)
    }

    override fun fetchItems(): Collection<TreeBuildData> {
        return AbilityBuildStorage.getAll()
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
}