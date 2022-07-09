package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

open class BonusEffectProperty(ability: Ability,
                               private val bonus: EffectBonus):
    AbilityProperty(ability), SetupProperty, ModifiableProperty {
    companion object: Factory {
        private const val TYPE_KEY: String = "type"
        private const val MODIFIER_KEY: String = "modifier"
        override fun create(ability: Ability, data: JsonElement): AbilityProperty {
            return BonusEffectProperty(ability, EffectBonus(data.asJsonObject))
        }
        override fun getKey(): String = "effect"
    }

    fun getEffectBonus(): EffectBonus = bonus

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun modify(entry: PropertyEntry) {
        entry.getProperty(getKey())?.let {
            val effect = (it as BonusEffectProperty).getEffectBonus().upgrade(getEffectBonus())
            entry.setProperty(getKey(), BonusEffectProperty(it.getAbility(), effect))
        }
    }

    override fun getTooltip(): List<Text> {
        val modifier = bonus.getEffectModifier()
        val text = Symbol.EFFECT.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_EFFECT.formatted(Formatting.GRAY).append(": "))
        if (modifier != null){
            text.append(LiteralText("${signed(modifier)}% ").formatted(Formatting.WHITE))
                .append(bonus.getEffectType().formatted(Formatting.GRAY))
        }else{
            text.append(bonus.getEffectType().formatted(Formatting.WHITE))
        }
        return listOf(text)
    }

    data class EffectBonus(private val type: EffectType,
                           private val modifier: Int?){
        constructor(json: JsonObject): this(
            if (json.has(TYPE_KEY)) EffectType.fromName(json[TYPE_KEY].asString)
                ?: EffectType.ENEMIES_SLOWNESS else EffectType.ENEMIES_SLOWNESS,
            if (json.has(MODIFIER_KEY)) json[MODIFIER_KEY].asInt else null
        )

        fun getEffectType(): EffectType = type

        fun getEffectModifier(): Int? = modifier

        fun upgrade(modifier: EffectBonus): EffectBonus {
            if (modifier.type != this.type)
                return this     //invalid modify (different type)
            val booster = if (modifier.modifier == null || this.modifier == null){
                null
            }else{
                this.modifier + modifier.modifier
            }
            return EffectBonus(this.type, booster)
        }
    }

    enum class EffectType: Translatable {
        ALLIES_RESISTANCE,
        ALLIES_DAMAGE,
        ALLIES_WALK_SPEED,
        ALLIES_ID_EFFECTIVENESS,
        ALLIES_INVINCIBLE,
        ENEMIES_RESISTANCE,
        ENEMIES_BLINDNESS,
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