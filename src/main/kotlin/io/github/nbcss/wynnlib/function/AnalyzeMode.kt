package io.github.nbcss.wynnlib.function

import io.github.nbcss.wynnlib.analysis.TooltipTransformer.Companion.asTransformer
import io.github.nbcss.wynnlib.analysis.TransformableItem
import io.github.nbcss.wynnlib.events.EventHandler
import io.github.nbcss.wynnlib.events.ItemLoadEvent
import io.github.nbcss.wynnlib.matcher.item.ItemMatcher.Companion.toItem
import io.github.nbcss.wynnlib.utils.ItemModifier
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString
import net.minecraft.text.Text

object AnalyzeMode: EventHandler<ItemLoadEvent> {
    const val KEY = "analyze_result"
    override fun handle(event: ItemLoadEvent) {
        val result = toItem(event.item)
        if (result is TransformableItem) {
            val transformer = asTransformer(event.item, result)
            if (transformer != null) {
                val tooltip = transformer.getTooltip()
                val list = NbtList()
                tooltip.forEach {
                    list.add(NbtString.of(Text.Serializer.toJson(it)))
                }
                ItemModifier.putElement(event.item, KEY, list)
            }
        }
    }

    fun getAnalyzeResult(item: ItemStack): List<Text>? {
        return ItemModifier.getElement(item, KEY) { elem ->
            return@getElement if (elem is NbtList) {
                elem.map { Text.Serializer.fromJson(it.asString()) as Text }
            }else{
                null
            }
        }
    }
}