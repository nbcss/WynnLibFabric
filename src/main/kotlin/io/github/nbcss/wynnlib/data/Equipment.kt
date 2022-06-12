package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.IntRange
import io.github.nbcss.wynnlib.utils.Keyed

interface Equipment : Keyed {
    fun getDisplayName(): String
    fun getTier(): ItemTier
    fun getIdentification(id: Identification): IntRange
}