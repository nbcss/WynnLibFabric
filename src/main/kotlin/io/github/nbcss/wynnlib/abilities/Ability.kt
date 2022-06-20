package io.github.nbcss.wynnlib.abilities

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed

class Ability(json: JsonObject): Keyed {
    private val id: String
    private val name: String
    init {
        id = json["id"].asString
        name = json["name"].asString
    }

    override fun getKey(): String = id

}