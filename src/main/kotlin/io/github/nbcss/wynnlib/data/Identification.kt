package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed

data class Identification(val name: String,         //name used in api data
                          val displayName: String,  //display name in game
                          val suffix: String,       //value suffix (e.g. %)
                          val inverted: Boolean     //whether the id bonus is inverted (e.g. -cost)
                          ): Keyed {
    constructor(json: JsonObject) : this(
        json.get("name").asString,
        json.get("displayName").asString,
        json.get("suffix").asString,
        if(json.has("inverted")) json.get("inverted").asBoolean else false)
    override fun getKey(): String = name
}
