package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.gui.widgets.CheckboxWidget
import io.github.nbcss.wynnlib.gui.widgets.ScrollPaneWidget
import io.github.nbcss.wynnlib.gui.widgets.SideTabWidget
import io.github.nbcss.wynnlib.gui.widgets.VerticalSliderWidget
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.items.identity.TooltipProvider
import io.github.nbcss.wynnlib.matcher.MatcherType
import io.github.nbcss.wynnlib.matcher.ProtectableType
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.formattingLines
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
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
    private val categories: MutableList<SettingCategory> = CategoryEnum.values().toMutableList()
    private val sideTabs: MutableList<SideTabWidget> = mutableListOf()
    private var category: SettingCategory = CategoryEnum.GENERAL
    private var scroll: ScrollPaneWidget? = null

    override fun getScroll(): ScrollPaneWidget? = scroll

    override fun init() {
        super.init()
        scroll = category.createScroll(this)
        sideTabs.clear()
        for ((index, category) in categories.withIndex()) {
            sideTabs.add(SideTabWidget.fromWindowSide(index, windowX, windowY, 40,
                SideTabWidget.Side.LEFT, category.getIcon(), object : SideTabWidget.Handler {
                    override fun onClick(index: Int) {
                        this@ConfigurationScreen.category = categories[index]
                        scroll = this@ConfigurationScreen.category.createScroll(this@ConfigurationScreen)
                        slider?.setSlider(0.0)
                    }

                    override fun isSelected(index: Int): Boolean {
                        return this@ConfigurationScreen.category == categories[index]
                    }

                    override fun drawTooltip(matrices: MatrixStack, mouseX: Int, mouseY: Int, index: Int) {
                        drawTooltip(matrices, listOf(categories[index].getDisplayText()), mouseX, mouseY)
                    }
                }))
        }
    }

    override fun getTitle(): Text {
        return super.getTitle().copy().append(LiteralText(" [").append(category.getDisplayText()).append("]"))
    }

    override fun drawBackgroundPre(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPre(matrices, mouseX, mouseY, delta)
        sideTabs.forEach { it.drawBackgroundPre(matrices, mouseX, mouseY) }
    }

    override fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPost(matrices, mouseX, mouseY, delta)
        sideTabs.forEach { it.drawBackgroundPost(matrices, mouseX, mouseY) }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (sideTabs.any { it.mouseClicked(mouseX.toInt(), mouseY.toInt(), button) })
            return true
        return super.mouseClicked(mouseX, mouseY, button)
    }

    enum class CategoryEnum: SettingCategory {
        GENERAL{
            private val icon: ItemStack = ItemFactory.fromEncoding("minecraft:oak_sign")
            override fun createScroll(screen: ConfigurationScreen): ScrollPaneWidget {
                return screen.GeneralScroll()
            }

            override fun getDisplayText(): Text {
                return LiteralText("General")
            }

            override fun getIcon(): ItemStack = icon
        },
        MATCHERS{
            private val icon: ItemStack = ItemFactory.fromEncoding("minecraft:map")
            override fun createScroll(screen: ConfigurationScreen): ScrollPaneWidget {
                return screen.MatcherScroll()
            }

            override fun getDisplayText(): Text {
                return LiteralText("Matchers")
            }

            override fun getIcon(): ItemStack = icon
        };
    }

    interface SettingCategory {
        fun createScroll(screen: ConfigurationScreen): ScrollPaneWidget
        fun getDisplayText(): Text
        fun getIcon(): ItemStack
    }


    inner class MatcherScroll: ScrollPaneWidget(null, this@ConfigurationScreen,
        scrollX, scrollY, SCROLL_WIDTH, SCROLL_HEIGHT) {
        private val protects: MutableMap<MatcherType, CheckboxWidget> = linkedMapOf()
        private val scrollHeight: Int
        init {
            val posX = 2
            var posY = 2
            val desc = object: TooltipProvider {
                override fun getTooltip(): List<Text> {
                    return listOf(LiteralText("Item Protection").formatted(Formatting.GRAY))
                }
            }
            for (type in MatcherType.getTypes()) {
                if (type !is ProtectableType)
                    continue
                val checked = type.isProtected()
                val checkbox = CheckboxWidget(posX, posY, type.getDisplayText(),
                    this@ConfigurationScreen, checked, desc)
                checkbox.setCallback {
                    type.setProtected(it.isChecked())
                }
                protects[type] = checkbox
                posY += 20
            }
            scrollHeight = posY + 2
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
            for (entry in protects.entries) {
                entry.value.updatePosition(posX, posY)
                entry.value.setIntractable(mouseOver)
                entry.value.render(matrices, mouseX, mouseY, delta)
                client.textRenderer.drawWithShadow(matrices, entry.key.getDisplayText(),
                    entry.value.x + 22.0F, entry.value.y + 5.0F, 0xFFFFFF)
            }
        }

        override fun getSlider(): VerticalSliderWidget? = slider

        override fun onContentClick(mouseX: Double, mouseY: Double, button: Int): Boolean {
            for (widget in protects.values) {
                widget.mouseClicked(mouseX, mouseY, button)
            }
            return false
        }

        override fun getContentHeight(): Int {
            return scrollHeight
        }
    }

    inner class GeneralScroll: ScrollPaneWidget(null, this@ConfigurationScreen,
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