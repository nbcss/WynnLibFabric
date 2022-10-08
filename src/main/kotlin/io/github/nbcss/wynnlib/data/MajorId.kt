package io.github.nbcss.wynnlib.data

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.items.identity.TooltipProvider
import io.github.nbcss.wynnlib.registry.Registry
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.wrapLines
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

data class MajorId(val id: String,
                   val displayName: String): Keyed, Translatable, TooltipProvider {
    constructor(json: JsonObject) : this(
        json.get("id").asString,
        json.get("displayName").asString,
    )

    companion object: Registry<MajorId>() {
        private const val RESOURCE = "assets/wynnlib/data/MajorID.json"
        private val nameMap: MutableMap<String, MajorId> = mutableMapOf()

        override fun getFilename(): String = RESOURCE

        override fun reload(array: JsonArray) {
            nameMap.clear()
            super.reload(array)
        }

        override fun put(item: MajorId) {
            super.put(item)
            nameMap[item.displayName] = item
        }

        override fun read(data: JsonObject): MajorId {
            return MajorId(data)
        }

        fun fromDisplayName(name: String): MajorId? {
            return nameMap[name]
        }
    }

    override fun getKey(): String = id

    override fun getTranslationKey(label: String?): String {
        val key = id.lowercase()
        if (label == "desc"){
            return "wynnlib.major_id.desc.$key"
        }
        return "wynnlib.major_id.name.$key"
    }

    override fun getTooltip(): List<Text> {
        val text = LiteralText("+")
            .append(formatted(Formatting.AQUA))
            .append(":ÀÀ")
            .append(formatted(Formatting.DARK_AQUA, "desc")).string
        return wrapLines(LiteralText(text).formatted(Formatting.AQUA), 190)
    }
}