package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.Keyed

data class AttackSpeed(val name: String,
                       val displayName: String,
                       val speedModifier: Double): Keyed {
    override fun getKey(): String = name
}
