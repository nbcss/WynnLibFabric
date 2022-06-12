package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.Keyed

data class Identification(val name: String,
                          val suffix: String): Keyed {
    override fun getKey(): String = name
}
