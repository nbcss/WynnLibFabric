package io.github.nbcss.wynnlib.utils

import com.google.gson.*
import io.github.nbcss.wynnlib.WynnLibEntry
import java.io.*

object FileUtils {
    private val GSON: Gson = GsonBuilder().setPrettyPrinting().create()
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

    fun readFile(path: String): JsonObject? {
        try {
            val file = File(path)
            if (file.exists()) {
                val json: JsonElement = JsonParser.parseReader(
                    BufferedReader(InputStreamReader(FileInputStream(file), "UTF-8")))
                return json.asJsonObject
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun writeFile(path: String, data: JsonObject) {
        try {
            val file = File(path)
            if (!file.exists()) {
                file.parentFile.mkdirs()
                file.createNewFile()
            }
            BufferedWriter(OutputStreamWriter(FileOutputStream(file), "UTF-8")).use {
                it.write(GSON.toJson(data))
                it.flush()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}