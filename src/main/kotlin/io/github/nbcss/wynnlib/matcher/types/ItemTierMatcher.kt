package io.github.nbcss.wynnlib.matcher.types

import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.matcher.MatcherType

class ItemTierMatcher(val tier: Tier): MatcherType {
    override fun getKey(): String {
        return "TIER:" + tier.name
    }
}