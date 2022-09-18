package io.github.nbcss.wynnlib.matcher

import io.github.nbcss.wynnlib.items.BaseItem

interface MatchableItem {
    fun getMatcherType(): MatcherType
    fun asBaseItem(): BaseItem?
}