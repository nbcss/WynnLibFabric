package io.github.nbcss.wynnlib.matcher

import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.utils.Keyed

interface MatcherType: Keyed {

    companion object {
        private val typeMap: MutableMap<String, MatcherType> = linkedMapOf()

        fun register(type: MatcherType): MatcherType {
            typeMap[type.getKey()] = type
            return type
        }

        fun fromTier(tier: Tier): MatcherType? {
            return null
        }
    }
}