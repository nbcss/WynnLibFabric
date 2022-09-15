package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.items.Box
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object BoxMatcher : ItemMatcher {
    private final val LV_RANGE_PATTERN = Regex("(\\d{1,3})-(\\d{1,3})")

    override fun toItem(item: ItemStack, name: String, tooltip: List<Text>): Box? {
        if (name.contains("Unidentified")) {
            var levelRange: IRange = SimpleIRange(0, 0)
            var tier: Tier = Tier.NORMAL
            var type: EquipmentType = EquipmentType.INVALID
            tooltip.forEach {
                if (it.string.contains("Range:")) {
                    levelRange = LV_RANGE_PATTERN.find(it.string)?.let { match ->
                        SimpleIRange(match.groupValues[1].toInt(), match.groupValues[2].toInt())
                    } ?: return null
                }
                if (it.string.contains("Tier:")) {
                    tier = Tier.valueOf(it.siblings[0].siblings[2].string.uppercase())
                }
                if (it.string.contains("Type:")) {
                    type = EquipmentType.valueOf(it.siblings[0].siblings[2].string.uppercase())
                }
            }
            return Box(type, tier, levelRange)
        } else {
            return null
        }
    }
}