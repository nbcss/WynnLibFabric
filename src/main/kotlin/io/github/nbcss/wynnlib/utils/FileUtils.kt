package io.github.nbcss.wynnlib.utils

import com.google.gson.JsonParser
import io.github.nbcss.wynnlib.WynnLibEntry
import io.github.nbcss.wynnlib.registry.Registry
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader

object FileUtils {
    fun <T: Keyed> loadRegistry(registry: Registry<T>, filename: String) {
        val reader = InputStreamReader(getResource(filename)!!, "utf-8")
        registry.reload(JsonParser().parse(reader).asJsonObject)
    }

    fun getResource(filename: String): InputStream? {
        return try {
            val url = WynnLibEntry.javaClass.classLoader.getResource(filename)
            if (url == null) {
                null
            } else {
                val connection = url.openConnection()
                connection.useCaches = false
                connection.getInputStream()
            }
        } catch (var4: IOException) {
            null
        }
    }
}