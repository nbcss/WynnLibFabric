package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class BonusEffectProperty(ability: Ability, data: JsonElement): AbilityProperty(ability) {
    companion object: Factory {
        private const val TYPE_KEY: String = "type"
        private const val MODIFIER_KEY: String = "modifier"
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return BonusEffectProperty(ability, data)
        }
        override fun getKey(): String = "effect"
    }
    private val bonus: EffectBonus = EffectBonus(data.asJsonObject)

    fun getEffectBonus(): EffectBonus = bonus

    override fun getTooltip(): List<Text> {
        return listOf(Symbol.EFFECT.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_EFFECT.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("${signed(bonus.getEffectModifier())}% ").formatted(Formatting.WHITE))
            .append(bonus.getEffectType().formatted(Formatting.GRAY)))
    }

    data class EffectBonus(private val type: EffectType,
                           private val modifier: Int){
        constructor(json: JsonObject): this(
            if (json.has(TYPE_KEY)) EffectType.fromName(json[TYPE_KEY].asString)
                ?: EffectType.ENEMIES_SLOWNESS else EffectType.ENEMIES_SLOWNESS,
            if (json.has(MODIFIER_KEY)) json[MODIFIER_KEY].asInt else 0
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