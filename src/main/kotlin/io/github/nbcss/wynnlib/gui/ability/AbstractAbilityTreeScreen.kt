package io.github.nbcss.wynnlib.gui.ability

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.AbilityTree
import io.github.nbcss.wynnlib.abilities.Archetype
import io.github.nbcss.wynnlib.abilities.properties.info.UpgradeProperty
import io.github.nbcss.wynnlib.gui.HandbookTabScreen
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.render.RenderKit
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
        val ICON: ItemStack = ItemFactory.fromEncoding("minecraft:stone_axe#83")
        val TEXTURE = Identifier("wynnlib", "textures/gui/ability_tree_viewer.png")
        val TITLE: Text = Translations.UI_ABILITY_TREE.translate()
        const val GRID_SIZE: Int = 24
        const val VIEW_WIDTH = 232
        const val VIEW_HEIGHT = 138
        const val SCROLL_FRAMES = 5
        val ACTIVE_OUTER_COLOR: AlphaColor = Color(0x37ACB5).toSolidColor()
        val ACTIVE_INNER_COLOR: AlphaColor = Color(0x5FD6DF).toSolidColor()
        val LOCKED_OUTER_COLOR: AlphaColor = Color(0x2D2E30).toSolidColor()
        val LOCKED_INNER_COLOR: AlphaColor = Color(0x252527).toSolidColor()
        val BASIC_OUTER_COLOR: AlphaColor = Color(0xEEEEEE).toSolidColor()
        val BASIC_INNER_COLOR: AlphaColor = Color(0xAAAAAA).toSolidColor()
    }
    protected var viewerX: Int = 0
    protected var viewerY: Int = 0
    private var scroll: Int = 0
    private var renderScrollPos: Int = 0
    private var scrollTicks: Int = -1

    abstract fun getAbilityTree(): AbilityTree

    abstract fun renderViewer(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float)

    abstract fun renderExtra(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float)

    fun renderAbilityTooltip(matrices: MatrixStack,
                             mouseX: Int,
                             mouseY: Int,
                             ability: Ability,
                             tooltip: MutableList<Text> = ability.getTooltip().toMutableList()) {
        var icon: Identifier? = null
        var overlay: String? = null
        val metadata = ability.getMetadata()
        if (tooltip.size >= 2) {
            if(metadata != null) {
                icon = metadata.getTexture()
            }else{
                UpgradeProperty.from(ability)?.let { property ->
                    property.getUpgradingAbility()?.let {
                        icon = it.getMetadata()?.getTexture()
                        overlay = "★"
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
            RenderKit.renderTexture(matrices, icon!!, x, y,
                0, 0, 18, 18, 18, 18)
            if (overlay != null) {
                matrices.push()
                matrices.translate(0.0, 0.0, 500.0)
                RenderKit.renderOutlineText(matrices, LiteralText(overlay),
                    (x + 10).toFloat(), (y + 10).toFloat(), Color.ORANGE, Color.YELLOW)
                matrices.pop()
            }
            RenderSystem.enableDepthTest()
        }
    }

    fun isOverViewer(mouseX: Int, mouseY: Int): Boolean {
        return mouseX >= viewerX && mouseX < viewerX + VIEW_WIDTH && mouseY >= viewerY && mouseY < viewerY + VIEW_HEIGHT
    }

    fun setScroll(targetScroll: Int) {
        val max = max(0, 8 + (1 + getAbilityTree().getMaxHeight()) * GRID_SIZE - VIEW_HEIGHT)
        scroll = MathHelper.clamp(targetScroll, 0, max)
        scrollTicks = SCROLL_FRAMES
    }

    fun drawOuterEdge(matrices: MatrixStack, from: Pos, to: Pos, color: Int, reroute: Boolean){
        RenderSystem.enableDepthTest()
        if (from.x != to.x){
            val posY = if (reroute) to.y else from.y
            DrawableHelper.fill(matrices, from.x, posY - 2, to.x, posY + 2, color)
        }
        if (from.y != to.y){
            val fromY = from.y + if (from.x == to.x || reroute) 0 else -2
            val toY = to.y + if (from.x != to.x && reroute) 2 else 0
            val posX = if (reroute) from.x else to.x
            DrawableHelper.fill(matrices, posX - 2, fromY, posX + 2, toY, color)
        }
    }

    fun drawInnerEdge(matrices: MatrixStack, from: Pos, to: Pos, color: Int, reroute: Boolean){
        if (from.x != to.x){
            val posY = if (reroute) to.y else from.y
            DrawableHelper.fill(matrices, from.x, posY - 1, to.x, posY + 1, color)
        }
        if (from.y != to.y){
            val fromY = from.y + if (from.x == to.x || reroute) 0 else -1
            val toY = to.y + if (from.x != to.x && reroute) 1 else 0
            val posX = if (reroute) from.x else to.x
            DrawableHelper.fill(matrices, posX - 1, fromY, posX + 1, toY, color)
        }
    }

    fun renderEdges(abilities: List<Ability>, matrices: MatrixStack, color: AlphaColor, inner: Boolean){
        abilities.forEach {
            val to = toScreenPosition(it.getHeight(), it.getPosition())
            it.getPredecessors().forEach { node ->
                val from = toScreenPosition(node.getHeight(), node.getPosition())
                val height = min(it.getHeight(), node.getHeight())
                val reroute = getAbilityTree().getAbilityFromPosition(height, it.getPosition()) != null
                if (inner) {
                    drawInnerEdge(matrices, from, to, color.getColorCode(), reroute)
                }else{
                    drawOuterEdge(matrices, from, to, color.getColorCode(), reroute)
                }
            }
        }
    }

    fun renderArchetypeIcon(matrices: MatrixStack, archetype: Archetype, x: Int, y: Int) {
        val icon = archetype.getTexture()
        val iconText = LiteralText(archetype.getIconText())
            .formatted(Formatting.BOLD).formatted(archetype.getFormatting())
        itemRenderer.renderInGuiWithOverrides(icon, x, y)
        //itemRenderer.renderGuiItemOverlay(textRenderer, icon, archetypeX, archetypeY, iconText)
        matrices.push()
        matrices.translate(0.0, 0.0, 200.0)
        RenderKit.renderOutlineText(matrices, iconText, x.toFloat() + 10, y.toFloat() + 9)
        matrices.pop()
    }

    fun toScreenPosition(height: Int, position: Int): Pos {
        return toScreenPosition(height, position, renderScrollPos)
    }

    private fun toScreenPosition(height: Int, position: Int, scrollPos: Int): Pos {
        val posX = windowX + 10 + GRID_SIZE / 2 + (position + 4) * GRID_SIZE
        val posY = windowY + 47 + GRID_SIZE / 2 + height * GRID_SIZE - scrollPos
        return Pos(posX, posY)
    }

    fun isOverNode(node: Pos, mouseX: Int, mouseY: Int): Boolean {
        return abs(node.x - mouseX) <= 11 && abs(node.y - mouseY) <= 11
    }

    fun resetScroll() {
        this.scroll = 0
        this.renderScrollPos = 0
        this.scrollTicks = -1
    }

    fun renderArchetypeOutline(matrices: MatrixStack, ability: Ability, x: Int, y: Int) {
        if (ability.getTier().getLevel() != 0){
            ability.getArchetype()?.let { arch ->
                val color = Color.fromFormatting(arch.getFormatting())
                val itemX = x - 15
                val itemY = y - 15
                val u = 32 + 30 * (ability.getTier().getLevel() - 1)
                RenderKit.renderTextureWithColor(
                    matrices, TEXTURE, color.toAlphaColor(165), itemX, itemY,
                    u, 144, 30, 30, 256, 256
                )
            }
        }
    }

    fun focusOnAbility(ability: Ability) {
        val currPos = toScreenPosition(ability.getHeight(), ability.getPosition())
        val diff = (height / 2) - currPos.y
        val scale = client!!.window.scaleFactor
        setScroll(scroll - diff)
        val endPos = toScreenPosition(ability.getHeight(), ability.getPosition(), scroll)
        InputUtil.setCursorParameters(client!!.window.handle, 212993,
            endPos.x.toDouble() * scale, endPos.y.toDouble() * scale)
    }

    open fun onClickNode(ability: Ability, button: Int): Boolean {
        val dependency = ability.getAbilityDependency()
        if (button == 1 && dependency != null) {
            playSound(SoundEvents.ENTITY_ITEM_PICKUP)
            focusOnAbility(dependency)
            return true
        }else{
            playSound(SoundEvents.ENTITY_SHULKER_HURT_CLOSED)
        }
        return false
    }

    override fun init() {
        super.init()
        viewerX = windowX + 7
        viewerY = windowY + 45
    }

    override fun tick() {
        super.tick()
        scrollTicks = max(-1, scrollTicks - 1)
    }

    override fun drawBackgroundPost(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        super.drawBackgroundPost(matrices, mouseX, mouseY, delta)
        RenderKit.renderTexture(matrices, TEXTURE, windowX + 4, windowY + 42, 0, 0, 238, 144)
    }

    override fun drawContents(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        //update moving scroll
        if (scrollTicks >= 0){
            renderScrollPos += ((scroll - renderScrollPos).toFloat() / scrollTicks.toFloat()).toInt()
        }else {
            renderScrollPos = scroll
        }
        val bottom = (viewerY + VIEW_HEIGHT)
        val scale = client!!.window.scaleFactor
        RenderSystem.enableScissor((viewerX * scale).toInt(),
            client!!.window.height - (bottom * scale).toInt(),
            (VIEW_WIDTH * scale).toInt(), (VIEW_HEIGHT * scale).toInt())
        renderViewer(matrices!!, mouseX, mouseY, delta)
        RenderSystem.disableScissor()
        renderExtra(matrices, mouseX, mouseY, delta)
        //DrawableHelper.fill(matrices, mouseX, mouseY, mouseX + 45, mouseY + 175, 0xFFFFFFF)
    }

    override fun mouseScrolled(mouseX: Double, mouseY: Double, amount: Double): Boolean {
        if (isOverViewer(mouseX.toInt(), mouseY.toInt())){
            setScroll(scroll - amount.toInt() * GRID_SIZE)
        }
        return super.mouseScrolled(mouseX, mouseY, amount)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        if (isOverViewer(mouseX.toInt(), mouseY.toInt())) {
            for (ability in getAbilityTree().getAbilities()) {
                val node = toScreenPosition(ability.getHeight(), ability.getPosition())
                if (isOverNode(node, mouseX.toInt(), mouseY.toInt())){
                    return onClickNode(ability, button)
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button)
    }
}