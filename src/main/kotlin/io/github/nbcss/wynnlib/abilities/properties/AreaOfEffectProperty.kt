package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.range.DRange
import io.github.nbcss.wynnlib.utils.range.SimpleDRange

interface AreaOfEffectProperty {
    companion object {
        const val AOE_KEY: String = "aoe"
        const val AOE_SHAPE_KEY: String = "aoe_shape"
        fun read(data: JsonObject): AreaOfEffect = AreaOfEffect(data)
    }

    fun getAreaOfEffect(): AreaOfEffect

    data class AreaOfEffect(private val range: DRange,
                            private val shape: Shape?){
        constructor(data: JsonObject): this(
            if (data.has(AOE_KEY)) SimpleDRange.fromString(data[AOE_KEY].asString) else DRange.ZERO,
            if (data.has(AOE_SHAPE_KEY)) Shape.fromName(data[AOE_SHAPE_KEY].asString) else null
        )

        fun getRange(): DRange = range

        fun getShape(): Shape? = shape
    }

    enum class Shape: Translatable {
        CIRCLE, CONE, LINE;
        companion object {
            private val VALUE_MAP: Map<String, Shape> = mapOf(
                pairs = values().map { it.name.uppercase() to it }.toTypedArray())
            fun fromName(name: String): Shape? = VALUE_MAP[name.uppercase()]
        }

        override fun getTranslationKey(label: String?): String {
            return "wynnlib.tooltip.ability.aoe_shape.${name.lowercase()}"
        }
    }
}