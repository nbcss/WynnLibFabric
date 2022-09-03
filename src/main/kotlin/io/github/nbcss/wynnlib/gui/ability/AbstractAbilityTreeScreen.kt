package io.github.nbcss.wynnlib.gui.ability

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.Archetype
import io.github.nbcss.wynnlib.abilities.properties.info.UpgradeProperty
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.gui.widgets.ATreeScrollWidget
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.render.TextureData
import io.github.nbcss.wynnlib.utils.*
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.TooltipComponent
import net.minecraft.client.util.InputUtil
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundEvents
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.math.MathHelper
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

abstract class AbstractAbilityTreeScreen(parent: Screen?) : HandbookTabScreen(parent, TITLE) {
    companion object {
        val SLIDER: TextureData = TextureData(
            Identifier("wynnlib", "textures/gui/ability_tree_scroll.png"),
            0, 0, 9, 40)
        val ICON: ItemStack = ItemFactory.fromEncoding("minecraft:stone_axe#83")
        val TEXTURE = Identifier("wynnlib", "textures/gui/ability_tree_viewer.png")
        val UPGRADE_TEXTURE = Identifier("wynnlib", "textures/gui/upgrade_frames.png")
        val TITLE: Text = Translations.UI_ABILITY_TREE.translate()
        val ACTIVE_OUTER_COLOR: AlphaColor = Color(0x37ACB5).solid()
        val ACTIVE_INNER_COLOR: AlphaColor = Color(0x5FD6DF).solid()
        val LOCKED_OUTER_COLOR: AlphaColor = Color(0x2D2E30).solid()
        val LOCKED_INNER_COLOR: AlphaColor = Color(0x252527).solid()
    }
    protected var viewerX: Int = 0
    protected var viewerY: Int = 0
    private var scroll: Int = 0
    private var renderScrollPos: Int = 0
    private var scrollTicks: Int = -1

    abstract fun getAbilityTree(): AbilityTree

    abstract fun renderExtra(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float)

    abstract fun getViewer(): ATreeScrollWidget?

    fun renderAbilityTooltip(matrices: MatrixStack,
                             mouseX: Int,
                             mouseY: Int,
                             ability: Ability,
                             tooltip: MutableList<Text> = ability.getTooltip().toMutableList()) {
        var icon: Identifier? = null
        var upgrade = false
        val metadata = ability.getMetadata()
        if (tooltip.size >= 2) {
            if(metadata != null) {
                icon = metadata.getTexture()
            }else{
                UpgradeProperty.from(ability)?.let { property ->
                    property.getUpgradingAbility()?.let {
                        icon = it.getMetadata()?.getTexture()
                        upgrade = true
                    }
                }
            }
        }
        if (icon != null){
            tooltip[0] = LiteralText("     ").append(tooltip[0])
            tooltip[1] = LiteralText("     ").append(tooltip[1])
        }
        drawTooltip(matrices, tooltip, mouseX, mouseY + 20)
        if (icon != null) {
            var i = 0
            var j = if (tooltip.size == 1) -2 else 0

            val components = tooltip.map { TooltipComponent.of(it.asOrderedText()) }
            for (tooltipComponent in components) {
                val k = tooltipComponent.getWidth(textRenderer)
                if (k > i) {
                    i = k
                }
                j += tooltipComponent.height
            }

            var x = mouseX + 12
            var y = mouseY + 8

            if (x + i > width) {
                x -= 28 + i
            }

            if (y + j + 6 > height) {
                y = height - j - 6
            }
            RenderSystem.disableDepthTest()
            RenderSystem.enableBlend()
            RenderKit.renderTexture(matrices, icon!!, x, y,
                0, 0, 18, 18, 18, 18)
            if (upgrade) {
                RenderKit.renderAnimatedTexture(matrices, UPGRADE_TEXTURE,
                    x, y, 18, 18, 20, 50, 300)
            }
            RenderSystem.enableDepthTest()
        }
    }

    fun renderArchetypeIcon(matrices: MatrixStack, archetype: Archetype, x: Int, y: Int) {
        val icon = archetype.getTexture()
        val iconText = LiteralText(archetype.getIconText())
            .formatted(Formatting.BOLD).formatted(archetype.getFormatting())
        itemRenderer.renderInGuiWithOverrides(icon, x, y)
        matrices.push()
        matrices.translate(0.0, 0.0, 200.0)
        RenderKit.renderOutlineText(matrices, iconText, x.toFloat() + 10, y.toFloat() + 9)
        matrices.pop()
    }

    override fun init() {
        super.init()
        viewerX = windowX + 7
        viewerY = windowY + 45
    }

    override fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPost(matrices, mouseX, mouseY, delta)
        RenderKit.renderTexture(matrices, TEXTURE, windowX + 4, windowY + 42, 0, 0, 238, 144)
    }

    override fun drawContents(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        getViewer()?.render(matrices, mouseX, mouseY, delta)
        renderExtra(matrices!!, mouseX, mouseY, delta)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if (getViewer()?.mouseScrolled(mouseX, mouseY, amount) == true)
            return true
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun mouseDragged(mouseX: Double, mouseY: Double, button: Int, deltaX: Double, deltaY: Double): Boolean {
        if (getViewer()?.mouseDragged(mouseX, mouseY, button, 0.0, 0.0) == true)
            return true
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (getViewer()?.mouseClicked(mouseX, mouseY, button) == true)
            return true
        return super.mouseClicked(mouseX, mouseY, button)
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (getViewer()?.mouseReleased(mouseX, mouseY, button) == true)
            return true
        return super.mouseReleased(mouseX, mouseY, button)
    }
}