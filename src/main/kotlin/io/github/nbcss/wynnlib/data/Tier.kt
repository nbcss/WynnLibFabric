package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed
import kotlin.math.ceil

data class Tier(val name: String,
                val prefix: String,
                val displayName: String,
                val translateKey: String,
                val priceBase: Double?,
                val priceCoefficient: Double?): Keyed {
    constructor(json: JsonObject) : this(
        json.get("name").asString,
        json.get("prefix").asString,
        json.get("displayName").asString,
        json.get("translateKey").asString,
        if(json.has("priceBase")) json.get("priceBase").asDouble else null,
        if(json.has("priceCoefficient")) json.get("priceCoefficient").asDouble else null)

    override fun getKey(): String = name

    fun canIdentify(): Boolean {
        return priceBase != null && priceCoefficient != null
    }

    fun getIdentifyPrice(level: Int): Int {
        return if (canIdentify()) ceil(priceBase!! + level * priceCoefficient!!).toInt() else 0
    }
}
