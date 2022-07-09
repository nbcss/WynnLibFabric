package io.github.nbcss.wynnlib.abilities

interface PlaceholderContainer {
    fun getPlaceholder(key: String): String
    fun putPlaceholder(key: String, value: String)
}