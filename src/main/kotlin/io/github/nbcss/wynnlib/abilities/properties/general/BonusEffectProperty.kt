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
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

open class BonusEffectProperty(ability: Ability,
                               private val bonuses: Map<EffectType, Int?>):
    AbilityProperty(ability), SetupProperty, ModifiableProperty {
    companion object: Type<BonusEffectProperty> {
        override fun create(ability: Ability, data: JsonElement): BonusEffectProperty {
            //val bonus = EffectBonus(data.asJsonObject)
            val json = data.asJsonObject
            val bonuses: MutableMap<EffectType, Int?> = linkedMapOf()
            for (entry in json.entrySet()) {
                val type = EffectType.fromName(entry.key)
                if (type != null) {
                    bonuses[type] = entry.value.asInt
                }
            }
            return BonusEffectProperty(ability, bonuses)
        }
        override fun getKey(): String = "effects"
    }

    fun getEffectBonus(type: EffectType): Int? = bonuses[type]

    override fun setup(entry: PropertyEntry) {
        entry.setProperty(getKey(), this)
    }

    override fun modify(entry: PropertyEntry) {
        from(entry)?.let {
            val bonuses: MutableMap<EffectType, Int?> = mutableMapOf()
            for (effectType in EffectType.values()) {
                val modifier = getEffectBonus(effectType)
                val base = it.getEffectBonus(effectType)
                if (modifier != null && base != null){
                    bonuses[effectType] = upgrade(base, modifier)
                }else if(modifier != null){
                    bonuses[effectType] = modifier
                }else if(base != null){
                    bonuses[effectType] = base
                }
            }
            entry.setProperty(getKey(), BonusEffectProperty(it.getAbility(), bonuses))
        }
    }

    private fun upgrade(base: Int?, modifier: Int?): Int? {
        if (base == null)
            return modifier
        if (modifier == null)
            return base
        if (base != 0 && modifier != 0)
            return base + modifier
        return 0
    }

    open fun getBonusText(bonus: Pair<EffectType, Int?>): List<Text>? {
        val modifier = bonus.second ?: return null
        val text = Symbol.EFFECT.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_EFFECT.formatted(Formatting.GRAY).append(": "))
        if (modifier != 0){
            val color = if (bonus.first.isPositiveEffect() == modifier > 0) {
                Formatting.WHITE
            }else{
                Formatting.RED
            }
            text.append(LiteralText("${signed(modifier)}% ").formatted(color))
                .append(bonus.first.formatted(Formatting.GRAY))
        }else{
            text.append(bonus.first.formatted(Formatting.WHITE))
        }
        return listOf(text)
    }

    override fun getTooltip(provider: PropertyProvider): List<Text> {
        return bonuses.entries.mapNotNull { getBonusText(it.key to it.value) }.flatten()
    }

    enum class EffectType(private val positive: Boolean): Translatable {
        ALLIES_RESISTANCE(true),
        ALLIES_DAMAGE(true),
        ALLIES_WALK_SPEED(true),
        ALLIES_ID_EFFECTIVENESS(true),
        ALLIES_INVINCIBLE(true),
        SELF_INVISIBLE(true),
        SELF_RESISTANCE(true),
        SELF_DAMAGE(true),
        SELF_WALK_SPEED(true),
        SUMMONS_DAMAGE(true),
        ENEMIES_RESISTANCE(false),
        ENEMIES_BLINDNESS(true),
        ENEMIES_SLOWNESS(true);
        companion object {
            private val VALUE_MAP: Map<String, EffectType> = mapOf(
                pairs = values().map { it.name.uppercase() to it }.toTypedArray())
            fun fromName(name: String): EffectType? = VALUE_MAP[name.uppercase()]
        }

        fun isPositiveEffect(): Boolean = positive

        override fun getTranslationKey(label: String?): String {
            return "wynnlib.tooltip.ability.effect.${name.lowercase()}"
        }
    }
}