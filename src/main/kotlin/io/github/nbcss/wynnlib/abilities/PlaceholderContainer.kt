package io.github.nbcss.wynnlib.abilities

import java.util.function.Supplier

interface PlaceholderContainer {
    fun getPlaceholder(key: String): String
    fun putPlaceholder(key: String, value: Supplier<String>)
    fun putPlaceholder(key: String, value: String) {
        putPlaceholder(key) { value }
    }
}