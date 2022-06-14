package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed

data class Identification(val name: String,         //name used in equipment api data
                          val innerName: String,    //name used in ingredient api data
                          val displayName: String,  //display name in game
                          val translateKey: String, //key to translate
                          val suffix: String,       //value suffix (e.g. %)
                          val inverted: Boolean,    //whether the id bonus is inverted (e.g. -cost)
                          val constant: Boolean     //whether the id range is always fixed to base
                          ): Keyed {
    constructor(json: JsonObject) : this(
        json.get("name").asString,
        json.get("innerName").asString,
        json.get("displayName").asString,
        json.get("translateKey").asString,
        json.get("suffix").asString,
        if(json.has("inverted")) json.get("inverted").asBoolean else false,
        if(json.has("constant")) json.get("constant").asBoolean else false)
    override fun getKey(): String = name
}
