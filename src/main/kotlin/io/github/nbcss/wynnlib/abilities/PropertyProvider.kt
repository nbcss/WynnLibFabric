package io.github.nbcss.wynnlib.abilities

interface PropertyProvider {
    fun getProperty(key: String): String
    fun hasProperty(key: String): Boolean = getProperty(key) != ""
}