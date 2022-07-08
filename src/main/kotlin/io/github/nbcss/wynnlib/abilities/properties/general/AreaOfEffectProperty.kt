package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.range.DRange
import io.github.nbcss.wynnlib.utils.range.SimpleDRange
import io.github.nbcss.wynnlib.utils.removeDecimal
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class AreaOfEffectProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        private const val RANGE_KEY: String = "range"
        private const val SHAPE_KEY: String = "shape"
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return AreaOfEffectProperty(ability, data)
        }
        override fun getKey(): String = "aoe"

        private fun checkZero(aoe: AreaOfEffect): List<Text>? {
            if (aoe.getRange().isZero()){
                val tooltip: MutableList<Text> = ArrayList()
                aoe.getShape()?.let {
                    tooltip.add(Symbol.AOE.asText().append(" ")
                        .append(Translations.TOOLTIP_ABILITY_AREA_OF_EFFECT.formatted(Formatting.GRAY).append(": "))
                        .append(it.formatted(Formatting.WHITE)))
                }
                return tooltip
            }
            return null
        }
    }
    private val aoe: AreaOfEffect = AreaOfEffect(data.asJsonObject)

    fun getAreaOfEffect(): AreaOfEffect = aoe

    override fun getTooltip(): List<Text> {
        val range = aoe.getRange()
        checkZero(aoe)?.let { return it }
        val suffix = if(range.upper() <= 1)
            Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS
        var value = removeDecimal(range.lower())
        if(!range.isConstant()){
            value = "$value-${removeDecimal(range.upper())}"
        }
        //val value = suffix.formatted(Formatting.WHITE, null, if (range % 1.0 != 0.0) range else range.toInt())
        val text = Symbol.AOE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_AREA_OF_EFFECT.formatted(Formatting.GRAY).append(": "))
            .append(suffix.formatted(Formatting.WHITE, null, value))
        aoe.getShape()?.let {
            text.append(LiteralText(" (").formatted(Formatting.GRAY))
                .append(it.formatted(Formatting.GRAY))
                .append(LiteralText(")").formatted(Formatting.GRAY))
        }
        return listOf(text)
    }

    class Modifier(ability: Ability, data: JsonElement): AbilityProperty(ability) {
        companion object: Factory {
            override fun create(ability: Ability, data: JsonElement): AbilityProperty {
                return Modifier(ability, data)
            }
            override fun getKey(): String = "aoe_modifier"
            fun read(data: JsonObject): AreaOfEffect = AreaOfEffect(data)
        }
        private val modifier: AreaOfEffect = AreaOfEffect(data.asJsonObject)

        fun getAreaOfEffectModifier(): AreaOfEffect = modifier

        override fun getTooltip(): List<Text> {
            val range = modifier.getRange()
            checkZero(modifier)?.let { return it }
            val suffix = if(range.upper() <= 1)
                Translations.TOOLTIP_SUFFIX_BLOCK else Translations.TOOLTIP_SUFFIX_BLOCKS
            var value = (if (range.lower() > 0) "+" else "") + removeDecimal(range.lower())
            val color = if (range.lower() < 0) Formatting.RED else Formatting.GREEN
            if(!range.isConstant()){
                value = "$value-${removeDecimal(range.upper())}"
            }
            val text = Symbol.AOE.asText().append(" ")
                .append(Translations.TOOLTIP_ABILITY_AREA_OF_EFFECT.formatted(Formatting.GRAY).append(": "))
                .append(suffix.formatted(color, null, value))
            modifier.getShape()?.let {
                text.append(LiteralText(" (").formatted(Formatting.GRAY))
                    .append(it.translate().formatted(Formatting.GRAY))
                    .append(LiteralText(")").formatted(Formatting.GRAY))
            }
            return listOf(text)
        }
    }

    data class AreaOfEffect(private val range: DRange,
                            private val shape: Shape?){
        constructor(data: JsonObject): this(
            if (data.has(RANGE_KEY)) SimpleDRange.fromString(data[RANGE_KEY].asString) else DRange.ZERO,
            if (data.has(SHAPE_KEY)) Shape.fromName(data[SHAPE_KEY].asString) else null
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