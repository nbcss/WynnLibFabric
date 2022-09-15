package io.github.nbcss.wynnlib.analysis.transformers

import io.github.nbcss.wynnlib.analysis.TooltipTransformer
import io.github.nbcss.wynnlib.analysis.TransformableItem
import io.github.nbcss.wynnlib.items.UnidentifiedBox
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class UnidentifiedBoxTransformer(private val item: UnidentifiedBox): TooltipTransformer {
    companion object: TooltipTransformer.Factory {
        const val KEY = "UNID_BOX"
        override fun create(stack: ItemStack, item: TransformableItem): TooltipTransformer? {
            return if (item is UnidentifiedBox) UnidentifiedBoxTransformer(item) else null
        }

        override fun getKey(): String = KEY
    }
    override fun init() {

    }

    override fun getTooltip(): List<Text> {
        return item.getTooltip()
    }
}