package io.github.nbcss.wynnlib.function

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.ItemLoadEvent
import io.github.nbcss.wynnlib.events.RenderItemOverrideEvent
import io.github.nbcss.wynnlib.render.RenderKit
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.ItemModifier
import net.minecraft.client.MinecraftClient
import net.minecraft.client.item.TooltipContext
import net.minecraft.text.TextColor
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper
import java.util.regex.Pattern

object EmeraldPouchBarRender: EventHandler<RenderItemOverrideEvent> {
    private val client = MinecraftClient.getInstance()
    private const val key = "pouch_bar"
    object LoadListener: EventHandler<ItemLoadEvent> {
        override fun handle(event: ItemLoadEvent) {
            if (!event.item.name.string.matches("§aEmerald Pouch§2 \\[Tier .+].*".toRegex()))
                return
            var emeralds = 0
            var capacity = 0
            val tooltip = event.item.getTooltip(client.player, TooltipContext.Default.NORMAL)
            for (text in tooltip) {
                if (text.asString() == "" && text.siblings.isNotEmpty() && text.siblings[0].siblings.isNotEmpty()){
                    val head = text.siblings[0].siblings[0]
                    if (head.style.color == TextColor.fromFormatting(Formatting.GOLD) && head.style.isBold){
                        val pattern = Pattern.compile("^(\\d+)² $")
                        val matcher = pattern.matcher(head.asString())
                        if (matcher.find()) {
                            emeralds = matcher.group(1).toInt()
                        }
                    }else if(text.siblings[0].siblings.size > 1){
                        val pattern = Pattern.compile("^\\((\\d+)(stx|²|²½|¼²) Total\\)$")
                        val matcher = pattern.matcher(text.siblings[0].siblings[1].asString())
                        if (matcher.find()) {
                            capacity = when (matcher.group(2)) {
                                "stx" -> matcher.group(1).toInt() * 262144
                                "\u00BC\u00B2" -> matcher.group(1).toInt() * 4096
                                "\u00B2\u00BD" -> matcher.group(1).toInt() * 64
                                else -> matcher.group(1).toInt()
                            }
                        }
                    }
                }
            }
            if (capacity > 0) {
                val bar = MathHelper.clamp(emeralds / capacity.toDouble(), 0.0, 1.0)
                ItemModifier.putDouble(event.item, key, bar)
            }
        }
    }
    override fun handle(event: RenderItemOverrideEvent) {
        if (!Settings.getOption(Settings.SettingOption.EMERALD_POUCH_BAR))
            return
        ItemModifier.readDouble(event.item, key)?.let {
            val color: Int = Color.GREEN.code()
            RenderKit.renderItemBar(it, color, event.x, event.y)
        }
    }
}