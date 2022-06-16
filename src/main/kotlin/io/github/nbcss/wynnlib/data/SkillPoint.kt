package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed

data class SkillPoint(val name: String): Keyed {
    constructor(json: JsonObject) : this(json.get("name").asString)

    override fun getKey(): String = name
}