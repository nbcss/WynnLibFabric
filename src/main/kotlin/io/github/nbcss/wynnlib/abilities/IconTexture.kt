package io.github.nbcss.wynnlib.abilities

import com.google.gson.JsonParser
import io.github.nbcss.wynnlib.utils.FileUtils
import net.minecraft.util.Identifier
import java.io.InputStreamReader

class IconTexture(file: String) {
    companion object {
        private val UNKNOWN: IconTexture = IconTexture("unknown.png")
        private val iconMap: MutableMap<String, IconTexture> = mutableMapOf()
        private const val filename = "assets/wynnlib/textures/icons/_icons.json"

        fun reload() {
            iconMap.clear()
            val reader = InputStreamReader(FileUtils.getResource(filename)!!, "utf-8")
            val data = JsonParser.parseReader(reader).asJsonObject
            for (entry in data.entrySet()) {
                val file = entry.value.asString
                iconMap[entry.key.uppercase()] = if (file == "") UNKNOWN else IconTexture(entry.value.asString)
            }
        }

        fun fromName(name: String?): IconTexture {
            return if (name == null) UNKNOWN else iconMap[name.uppercase()] ?: UNKNOWN
        }
    }
    private val texture: Identifier = Identifier("wynnlib", "textures/icons/${file.lowercase()}")

    fun getTexture(): Identifier = texture
}