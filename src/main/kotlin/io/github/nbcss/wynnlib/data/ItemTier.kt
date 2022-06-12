package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.Keyed

data class ItemTier(val name: String): Keyed {
    override fun getKey(): String = name
}
