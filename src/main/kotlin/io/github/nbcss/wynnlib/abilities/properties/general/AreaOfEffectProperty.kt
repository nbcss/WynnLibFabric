package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.range.DRange
import io.github.nbcss.wynnlib.utils.range.SimpleDRange
import io.github.nbcss.wynnlib.utils.removeDecimal
import io.github.nbcss.wynnlib.utils.round
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class AreaOfEffectProperty(ability: Ability,
                           private val aoe: AreaOfEffect):
    AbilityProperty(ability), SetupProperty {
    companion object: Type<AreaOfEffectProperty> {
        private const val RANGE_KEY: String = "range"
        private const val SHAPE_KEY: String = "shape"
        override fun create(ability: Ability, data: JsonElement): AreaOfEffectProperty {
            return AreaOfEffectProperty(ability, AreaOfEffect(data.asJsonObject))
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

    fun getAreaOfEffect(): AreaOfEffect = aoe

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
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

    class Modifier(ability: Ability, data: JsonElement):
        AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Modifier> {
            override fun create(ability: Ability, data: JsonElement): Modifier {
                return Modifier(ability, data)
            }
            override fun getKey(): String = "aoe_modifier"
        }
        private val modifier: AreaOfEffect = AreaOfEffect(data.asJsonObject)

        fun getAreaOfEffectModifier(): AreaOfEffect = modifier

        override fun modify(entry: PropertyEntry) {
            AreaOfEffectProperty.from(entry)?.let {
                val aoe = it.getAreaOfEffect().upgrade(getAreaOfEffectModifier())
                entry.setProperty(AreaOfEffectProperty.getKey(), AreaOfEffectProperty(it.getAbility(), aoe))
            }
        }

        override fun getTooltip(provider: PropertyProvider): List<Text> {
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

    class Clear(ability: Ability): AbilityProperty(ability), ModifiableProperty {
        companion object: Type<Clear> {
            override fun create(ability: Ability, data: JsonElement): Clear {
                return Clear(ability)
            }
            override fun getKey(): String = "aoe_clear"
        }

        override fun modify(entry: PropertyEntry) {
            AreaOfEffectProperty.from(entry)?.let {
                entry.clearProperty(AreaOfEffectProperty.getKey())
            }
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

        fun upgrade(modifier: AreaOfEffect): AreaOfEffect {
            val lower = round(range.lower() + modifier.range.lower())
            val upper = round(range.upper() + modifier.range.upper())
            val shape = modifier.shape ?: this.shape
            return AreaOfEffect(SimpleDRange(lower, upper), shape)
        }
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