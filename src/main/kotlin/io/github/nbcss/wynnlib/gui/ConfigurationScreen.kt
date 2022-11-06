package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.gui.widgets.*
import io.github.nbcss.wynnlib.gui.widgets.buttons.*
import io.github.nbcss.wynnlib.gui.widgets.scrollable.CheckboxWidget
import io.github.nbcss.wynnlib.gui.widgets.scrollable.LabelWidget
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
import java.util.function.Supplier

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
                return Translations.SETTINGS_GENERAL.translate()
            }

            override fun getIcon(): ItemStack = icon
        },
        MATCHERS{
            private val icon: ItemStack = ItemFactory.fromEncoding("minecraft:map")
            override fun createScroll(screen: ConfigurationScreen): ScrollPaneWidget {
                return screen.MatcherScroll()
            }

            override fun getDisplayText(): Text {
                return Translations.SETTINGS_MATCHERS.translate()
            }

            override fun getIcon(): ItemStack = icon
        };
    }

    interface SettingCategory {
        fun createScroll(screen: ConfigurationScreen): ScrollPaneWidget
        fun getDisplayText(): Text
        fun getIcon(): ItemStack
    }

    inner class MatcherScroll: ElementsContainerScroll(null, this@ConfigurationScreen,
        scrollX, scrollY, SCROLL_WIDTH, SCROLL_HEIGHT) {
        //private val protects: MutableMap<MatcherType, CheckboxWidget> = linkedMapOf()
        //private val scrollHeight: Int
        init {
            val posX = 2
            var posY = 2
            val desc = object: TooltipProvider {
                override fun getTooltip(): List<Text> {
                    return listOf(Translations.MATCHER_ITEM_PROTECTION.formatted(Formatting.GRAY))
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
                addElement(checkbox)
                addElement(LabelWidget(posX + 22, posY + 5, Supplier {
                    return@Supplier type.getDisplayText()
                }))
                posY += 20
            }
            setContentHeight(posY + 2)
        }
        override fun getSlider(): VerticalSliderWidget? = slider
    }

    inner class GeneralScroll: ElementsContainerScroll(null, this@ConfigurationScreen,
        scrollX, scrollY, SCROLL_WIDTH, SCROLL_HEIGHT) {
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
                addElement(checkbox)
                addElement(LabelWidget(posX + 22, posY + 5, Supplier {
                    return@Supplier option.formatted(Formatting.GRAY)
                }))
                posY += 20
            }
            setContentHeight(posY + 2)
        }
        override fun getSlider(): VerticalSliderWidget? = slider
    }
}