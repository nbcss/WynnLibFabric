package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.data.SpellSlot

interface IdPropertyProvider {
    /**
     * Get id property of the given key, only used in spell id & charm exclusive id
     */
    fun getIdProperty(key: String): String? = null

    companion object: IdPropertyProvider {
        //default property, always provide blank spell name
        override fun getIdProperty(key: String): String? {
            SpellSlot.fromKey(key)?.let { spell ->
                return spell.translate().string
            }
            return null
        }
    }
}