package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class ElementMasteryProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        private const val ELEMENT_KEY: String = "element"
        private const val RAW_KEY: String = "raw"
        private const val PCT_KEY: String = "pct"
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return ElementMasteryProperty(ability, data)
        }
        override fun getKey(): String = "element_mastery"
    }
    private val booster: Booster = Booster(data.asJsonObject)

    fun getElementBooster(): Booster = booster

    override fun getTooltip(): List<Text> {
        val element = booster.getElement()
        val tooltip: MutableList<Text> = ArrayList()
        if(!booster.getRawBooster().isZero()){
            var value = "+${booster.getRawBooster().lower()}"
            if(!booster.getRawBooster().isConstant()){
                value = "$value-${booster.getRawBooster().upper()}"
            }
            tooltip.add(element.formatted(Formatting.GRAY, "tooltip.damage").append(": ")
                .append(LiteralText(value).formatted(Formatting.WHITE)))
        }
        if(booster.getPctBooster() != 0){
            tooltip.add(element.formatted(Formatting.GRAY, "tooltip.damage").append(": ")
                .append(LiteralText("+${booster.getPctBooster()}%").formatted(Formatting.WHITE)))
        }
        return tooltip
    }

    data class Booster(private val element: Element,
                       private val boosterRaw: IRange,
                       private val boosterPct: Int) {
        constructor(json: JsonObject): this(
            if (json.has(ELEMENT_KEY)) Element.fromId(json[ELEMENT_KEY].asString)?: Element.AIR else Element.AIR,
            if (json.has(RAW_KEY)) SimpleIRange.fromString(json[RAW_KEY].asString) else IRange.ZERO,
            if (json.has(PCT_KEY)) json[PCT_KEY].asInt else 0
        )

        fun getElement(): Element = element

        fun getRawBooster(): IRange = boosterRaw

        fun getPctBooster(): Int = boosterPct

        fun getPctBoosterRate(): Double = getPctBooster() / 100.0
    }
}