package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed

data class Element(val name: String,
                   val displayName: String,
                   val damageName: String,
                   val defenceName: String,
                   val damageBonusName: String,
                   val defenceBonusName: String): Keyed {
    constructor(json: JsonObject) : this(
        json.get("name").asString,
        json.get("displayName").asString,
        json.get("damageName").asString,
        json.get("defenceName").asString,
        json.get("damageBonusName").asString,
        json.get("defenceBonusName").asString)
    override fun getKey(): String = name
}