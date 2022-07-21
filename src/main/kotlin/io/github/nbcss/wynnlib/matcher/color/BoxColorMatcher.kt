package io.github.nbcss.wynnlib.matcher.color

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.utils.Color
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import java.util.function.Supplier

object BoxColorMatcher: ColorMatcher {
    //private val tierMap = mapOf(pairs = Tier.values().map { TextColor.fromFormatting(it.formatting) to it }.toTypedArray())

    override fun toRarityColor(item: ItemStack, tooltip: List<Text>): Supplier<Color?>? {
        for (text in tooltip) {
            if (text.siblings.isNotEmpty()){
                text.siblings.forEach(){
                    if (it.asString() == "Tier: "){
                        val tier = Tier.fromName(it.siblings.firstOrNull()?.asString()?.lowercase() ?: return null)
                        // 先摆了
                        return Supplier { Settings.getTierColor(tier) } // TODO 盒子使用单独的样式
                    }
                }
            }
        }

        return null
    }

}