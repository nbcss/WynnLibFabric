package io.github.nbcss.wynnlib.function

import com.mojang.blaze3d.systems.RenderSystem
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.ItemLoadEvent
import io.github.nbcss.wynnlib.events.RenderItemOverrideEvent
import io.github.nbcss.wynnlib.utils.ItemModifier
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.client.render.*
import net.minecraft.client.render.VertexFormat.DrawMode
import net.minecraft.util.math.MathHelper
import java.util.regex.Pattern
import kotlin.math.roundToInt

object DurabilityRender: EventHandler<RenderItemOverrideEvent> {
    private val pattern = Pattern.compile("\\[(\\d+)/(\\d+) Durability]")
    private val client = MinecraftClient.getInstance()
    private const val key = "crafted_durability"
    object LoadListener: EventHandler<ItemLoadEvent> {
        override fun handle(event: ItemLoadEvent) {
            val tooltip = event.item.getTooltip(client.player, TooltipContext.Default.NORMAL)
            val durability = tooltip.asSequence().filter { it.asString() == "" && it.siblings.isNotEmpty() }
                .map { it.siblings[0] }.filter { it.asString() == "" && it.siblings.size >= 2 }
                .filter { it.siblings[0].asString().contains("Crafted ") }
                .map { it.siblings[1].asString() }.firstNotNullOfOrNull {
                    val matcher = pattern.matcher(it)
                    var value: Double? = null
                    if (matcher.find()) {
                        val v1 = matcher.group(1).toDouble()
                        val v2 = matcher.group(2).toDouble()
                        value = if (v2 <= 0 || v1 > v2) 0.0 else v1 / v2
                    }
                    value
                }
            if (durability != null){
                ItemModifier.putDouble(event.item, key, durability)
            }
        }
    }
    override fun handle(event: RenderItemOverrideEvent) {
        if (!Settings.getOption(Settings.SettingOption.DURABILITY))
            return
        ItemModifier.readDouble(event.item, key)?.let {
            drawDurabilityBar(it, event.x, event.y)
        }
    }

    private fun drawDurabilityBar(durability: Double, x: Int, y: Int) {
        RenderSystem.disableDepthTest()
        RenderSystem.disableTexture()
        RenderSystem.disableBlend()
        val tessellator = Tessellator.getInstance()
        val bufferBuilder = tessellator.buffer
        val steps: Int = (durability * 13.0f).roundToInt()
        val color: Int = MathHelper.hsvToRgb(durability.toFloat() / 3.0f, 1.0f, 1.0f)
        renderGuiQuad(bufferBuilder, x + 2, y + 13, 13, 2, 0)
        renderGuiQuad(bufferBuilder, x + 2, y + 13, steps, 1, color)
        RenderSystem.enableBlend()
        RenderSystem.enableTexture()
        RenderSystem.enableDepthTest()
    }

    private fun renderGuiQuad(
        buffer: BufferBuilder,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        color: Int
    ) {
        val red = color shr 16 and 255
        val green = color shr 8 and 255
        val blue = color and 255
        val alpha = 255
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        buffer.begin(DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        buffer.vertex((x + 0).toDouble(), (y + 0).toDouble(), 0.0).color(red, green, blue, alpha).next()
        buffer.vertex((x + 0).toDouble(), (y + height).toDouble(), 0.0).color(red, green, blue, alpha).next()
        buffer.vertex((x + width).toDouble(), (y + height).toDouble(), 0.0).color(red, green, blue, alpha).next()
        buffer.vertex((x + width).toDouble(), (y + 0).toDouble(), 0.0).color(red, green, blue, alpha).next()
        buffer.end()
        BufferRenderer.draw(buffer)
    }
}