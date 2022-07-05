package io.github.nbcss.wynnlib.abilities.properties.legacy

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.i18n.Translatable

interface BonusEffectProperty {
    companion object {
        const val EFFECT_TYPE_KEY: String = "effect_type"
        const val EFFECT_MODIFIER_KEY: String = "effect_modifier"
        fun read(data: JsonObject): EffectBonus = EffectBonus(data)
    }

    fun getEffectBonus(): EffectBonus

    data class EffectBonus(private val type: EffectType,
                           private val modifier: Int){
        constructor(json: JsonObject): this(
            if (json.has(EFFECT_TYPE_KEY)) EffectType.fromName(json[EFFECT_TYPE_KEY].asString)
                ?: EffectType.ENEMIES_SLOWNESS else EffectType.ENEMIES_SLOWNESS,
            if (json.has(EFFECT_MODIFIER_KEY)) json[EFFECT_MODIFIER_KEY].asInt else 0
        )

        fun getEffectType(): EffectType = type

        fun getEffectModifier(): Int = modifier

        fun getEffectModifierRate(): Double = getEffectModifier() / 100.0
    }

    enum class EffectType: Translatable {
        ALLIES_RESISTANCE,
        ALLIES_DAMAGE,
        ALLIES_WALK_SPEED,
        ALLIES_ID_EFFECTIVENESS,
        ALLIES_INVINCIBLE,
        ENEMIES_RESISTANCE,
        ENEMIES_SLOWNESS;
        companion object {
            private val VALUE_MAP: Map<String, EffectType> = mapOf(
                pairs = values().map { it.name.uppercase() to it }.toTypedArray())
            fun fromName(name: String): EffectType? = VALUE_MAP[name.uppercase()]
        }

        override fun getTranslationKey(label: String?): String {
            return "wynnlib.tooltip.ability.effect.${name.lowercase()}"
        }
    }
}