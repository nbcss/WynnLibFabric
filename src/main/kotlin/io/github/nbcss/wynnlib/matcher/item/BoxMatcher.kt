package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.items.UnidentifiedBox
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

object BoxMatcher : ItemMatcher {
    private val LV_RANGE_PATTERN = Regex("(\\d{1,3})-(\\d{1,3})")

    override fun toItem(item: ItemStack, name: String, tooltip: List<Text>, inMarket: Boolean): UnidentifiedBox? {
        if (name.contains("Unidentified")) {
            var levelRange: IRange = SimpleIRange(0, 0)
            var tier: Tier? = null
            var type: EquipmentType = EquipmentType.INVALID
            try{
                tooltip.forEach {
                    if (it.string.contains("Range:")) {
                        levelRange = LV_RANGE_PATTERN.find(it.string)?.let { match ->
                            SimpleIRange(match.groupValues[1].toInt(), match.groupValues[2].toInt())
                        } ?: return null
                    }
                    if (it.string.contains("Tier:")) {
                        tier = Tier.fromId(it.siblings[0].siblings[2].string)
                    }
                    if (it.string.contains("Type:")) {
                        type = EquipmentType.fromDisplayName(it.siblings[0].siblings[2].string)
                    }
                }
            }catch (e: Exception) {
                return null
            }
            if (type != EquipmentType.INVALID) {
                tier?.let {
                    return UnidentifiedBox(type, it, levelRange)
                }
            }
            return null
        } else {
            return null
        }
    }
}