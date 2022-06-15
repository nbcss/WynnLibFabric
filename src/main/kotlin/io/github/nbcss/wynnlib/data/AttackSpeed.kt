package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed

data class AttackSpeed(val name: String,
                       val displayName: String,
                       val speedModifier: Double): Keyed {
    constructor(json: JsonObject) : this(
        json.get("name").asString,
        json.get("displayName").asString,
        json.get("speed").asDouble)
    override fun getKey(): String = name
}
