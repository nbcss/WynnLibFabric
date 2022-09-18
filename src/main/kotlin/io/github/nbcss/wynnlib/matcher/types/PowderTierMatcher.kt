package io.github.nbcss.wynnlib.matcher.types

import io.github.nbcss.wynnlib.items.Powder
import io.github.nbcss.wynnlib.matcher.AbstractMatcherType

class PowderTierMatcher(private val tier: Powder.Tier): AbstractMatcherType(tier.color) {
    companion object {
        fun keyOf(tier: Powder.Tier): String = "POWDER_TIER_" + tier.name
    }

    override fun getKey(): String = keyOf(tier)
}