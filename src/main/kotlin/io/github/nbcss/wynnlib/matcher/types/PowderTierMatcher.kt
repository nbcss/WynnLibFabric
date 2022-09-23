package io.github.nbcss.wynnlib.matcher.types

import io.github.nbcss.wynnlib.items.Powder
import io.github.nbcss.wynnlib.matcher.AbstractMatcherType
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class PowderTierMatcher(private val tier: Powder.Tier): AbstractMatcherType(tier.color) {
    companion object {
        fun keyOf(tier: Powder.Tier): String = "POWDER_TIER_" + tier.name
    }

    override fun getDisplayText(): Text {
        return formatted(Formatting.GOLD)
    }

    override fun getKey(): String = keyOf(tier)
}