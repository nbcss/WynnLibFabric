package io.github.nbcss.wynnlib.items.identity

import com.google.gson.JsonPrimitive

object ItemStarProperty {
    private const val STARRED_FIELD = "starred"

    fun hasStar(item: ConfigurableItem): Boolean {
        return ConfigurableItem.Modifier.has(item, STARRED_FIELD)
    }

    fun setStarred(item: ConfigurableItem, starred: Boolean) {
        if (starred) {
            ConfigurableItem.Modifier.write(item, STARRED_FIELD, JsonPrimitive(true))
        }else{
            ConfigurableItem.Modifier.clear(item, STARRED_FIELD)
        }
    }
}