package io.github.nbcss.wynnlib.items.identity

import io.github.nbcss.wynnlib.utils.Keyed

interface ConfigurableItem: Keyed {
    fun getConfigDomain(): String
    fun getConfigKey(): String = "${getConfigDomain()}:${getKey()}"
}