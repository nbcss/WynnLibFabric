package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.gui.widgets.CheckboxWidget
import io.github.nbcss.wynnlib.gui.widgets.ScrollPaneWidget
import io.github.nbcss.wynnlib.gui.widgets.VerticalSliderWidget
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.items.identity.TooltipProvider
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.formattingLines
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ConfigurationScreen(parent: Screen?) : GenericScrollScreen(parent, TITLE) {
    companion object {
        val ICON: ItemStack = ItemFactory.fromEncoding("minecraft:repeater")
        val TITLE: Text = Translations.UI_CONFIGURATION.translate()
        val FACTORY = object: TabFactory {
            override fun getTabIcon(): ItemStack = ICON
            override fun getTabTitle(): Text = TITLE
            override fun createScreen(parent: Screen?): HandbookTabScreen = ConfigurationScreen(parent)
            override fun isInstance(screen: HandbookTabScreen): Boolean = screen is ConfigurationScreen
            override fun shouldDisplay(): Boolean = true
        }
    }
    private var scroll: Scroll? = null

    override fun getScroll(): ScrollPaneWidget? = scroll

    override fun init() {
        super.init()
        scroll = Scroll()
    }

    inner class Scroll: ScrollPaneWidget(null, this@ConfigurationScreen,
        scrollX, scrollY, SCROLL_WIDTH, SCROLL_HEIGHT) {
        private val options: MutableMap<Settings.SettingOption, CheckboxWidget> = linkedMapOf()
        private val scrollHeight: Int
        init {
            val posX = 2
            var posY = 2
            for (option in Settings.SettingOption.values()) {
                val description = object: TooltipProvider {
                    override fun getTooltip(): List<Text> {
                        return formattingLines(option.translate("desc").string,
                            Formatting.GRAY.toString(), 200)
                    }
                }
                val checkbox = CheckboxWidget(posX, posY, option.formatted(Formatting.GOLD),
                    this@ConfigurationScreen, Settings.getOption(option), description)
                checkbox.setCallback {
                    Settings.setOption(option, it.isChecked())
                }
                options[option] = checkbox
                posY += 20
            }
            scrollHeight = posY + 2
        }

        override fun getSlider(): VerticalSliderWidget? = slider

        override fun onContentClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            for (widget in options.values) {
                widget.mouseClicked(mouseX, mouseY, button)
            }
            return false
        }

        override fun renderContents(
            matrices: MatrixStack,
            mouseX: Int,
            mouseY: Int,
            position: Double,
            delta: Float,
            mouseOver: Boolean
        ) {
            val posX = x
            val posY = (y - position).toInt()
            for (entry in options.entries) {
                entry.value.updatePosition(posX, posY)
                entry.value.setIntractable(mouseOver)
                entry.value.render(matrices, mouseX, mouseY, delta)
                client.textRenderer.drawWithShadow(matrices, entry.key.formatted(Formatting.GRAY),
                    entry.value.x + 22.0F, entry.value.y + 5.0F, 0xFFFFFF)
            }
        }

        override fun getContentHeight(): Int = scrollHeight
    }
}