package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.lang.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
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
    override fun getKey(): String = id

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

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.id." + getKey().lowercase(Locale.getDefault())
    }
}
