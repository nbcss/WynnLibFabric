package io.github.nbcss.wynnlib.function

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.ItemLoadEvent
import io.github.nbcss.wynnlib.events.RenderItemOverrideEvent
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.ItemModifier
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.util.math.MathHelper
import java.util.regex.Pattern

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
            val color: Int = MathHelper.hsvToRgb(it.toFloat() / 3.0f, 1.0f, 1.0f)
            RenderKit.renderItemBar(it, color, event.x, event.y)
        }
    }
}