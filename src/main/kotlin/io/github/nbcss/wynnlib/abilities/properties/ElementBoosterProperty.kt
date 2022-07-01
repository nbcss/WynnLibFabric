package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange

interface ElementBoosterProperty {
    companion object {
        const val ELEMENT_KEY: String = "element"
        const val BOOSTER_RAW_KEY: String = "raw_booster"
        const val BOOSTER_PCT_KEY: String = "pct_booster"
        fun read(data: JsonObject): Booster = Booster(data)
    }

    fun getElementBooster(): Booster

    data class Booster(private val element: Element,
                       private val boosterRaw: IRange,
                       private val boosterPct: Int) {
        constructor(json: JsonObject): this(
            if (json.has(ELEMENT_KEY)) Element.fromId(json[ELEMENT_KEY].asString)?: Element.AIR else Element.AIR,
            if (json.has(BOOSTER_RAW_KEY)) SimpleIRange.fromString(json[BOOSTER_RAW_KEY].asString) else IRange.ZERO,
            if (json.has(BOOSTER_PCT_KEY)) json[BOOSTER_PCT_KEY].asInt else 0
        )

        fun getElement(): Element = element

        fun getRawBooster(): IRange = boosterRaw

        fun getPctBooster(): Int = boosterPct

        fun getPctBoosterRate(): Double = getPctBooster() / 100.0
    }
}