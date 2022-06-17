package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed

data class Identification(val id: String,               //id used in equipment api data
                          val innerName: String,        //name used in ingredient api data
                          val displayName: String,      //display name in game
                          val translationKey: String,   //key to translate
                          val suffix: String,           //value suffix (e.g. %)
                          val inverted: Boolean,        //whether the id bonus is inverted (e.g. -cost)
                          val constant: Boolean         //whether the id range is always fixed to base
                          ): Keyed {
    constructor(json: JsonObject) : this(
        json.get("id").asString,
        json.get("innerName").asString,
        json.get("displayName").asString,
        json.get("translationKey").asString,
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
}
