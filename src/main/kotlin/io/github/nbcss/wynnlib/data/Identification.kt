package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.Version
import java.util.*

data class Identification(val id: String,               //id used in translation key
                          val apiId: String,            //id used in equipment api data
                          val name: String,             //name used in ingredient api data
                          val displayName: String,      //display name in game
                          val suffix: String,           //value suffix (e.g. %)
                          val inverted: Boolean,        //whether the id bonus is inverted (e.g. -cost)
                          val constant: Boolean         //whether the id range is always fixed to base
                          ): Keyed, Translatable {
    constructor(json: JsonObject) : this(
        json.get("id").asString,
        json.get("apiId").asString,
        json.get("name").asString,
        json.get("displayName").asString,
        json.get("suffix").asString,
        if(json.has("inverted")) json.get("inverted").asBoolean else false,
        if(json.has("constant")) json.get("constant").asBoolean else false)

    companion object {
        private val API_ID_MAP: MutableMap<String, Identification> = LinkedHashMap()
        private val NAME_MAP: MutableMap<String, Identification> = LinkedHashMap()
        private var version: Version? = null

        fun reload(json: JsonObject) {
            val ver = Version(json.get("version").asString)
            //skip reload if currently have newer version
            if(version != null && version!! > ver) return
            //reload identifications
            API_ID_MAP.clear()
            NAME_MAP.clear()
            json.get("data").asJsonArray.forEach{
                val id = Identification(it.asJsonObject)
                API_ID_MAP[id.apiId.lowercase(Locale.getDefault())] = id
                NAME_MAP[id.name.uppercase(Locale.getDefault())] = id
            }
            version = ver
        }

        fun fromApiId(name: String): Identification? {
            return API_ID_MAP[name.lowercase(Locale.getDefault())]
        }

        fun fromName(name: String): Identification? {
            return NAME_MAP[name.uppercase(Locale.getDefault())]
        }

        fun getAll(): List<Identification> {
            return API_ID_MAP.values.toList()
        }
    }

    override fun getKey(): String = id

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.id.${getKey().lowercase(Locale.getDefault())}"
    }

    enum class Group {
        SKILL_POINT_BONUS,
        COMBAT,
        SURVIVABILITY,
        ELEMENT_DAMAGE_BONUS,
        ELEMENT_DEFENCE_BONUS,
        MANEUVERABILITY,
        MISC,
        SPELL_COST,
    }
}
