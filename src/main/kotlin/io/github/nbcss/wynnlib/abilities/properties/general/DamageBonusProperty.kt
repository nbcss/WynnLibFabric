package io.github.nbcss.wynnlib.abilities.properties.general

import com.google.gson.JsonElement
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.entries.PropertyEntry
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.ModifiableProperty
import io.github.nbcss.wynnlib.abilities.properties.SetupProperty
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.utils.Symbol
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

open class DamageBonusProperty(ability: Ability,
                               protected val bonus: Int): AbilityProperty(ability) {
    companion object: Type<DamageBonusProperty> {
        override fun create(ability: Ability, data: JsonElement): DamageBonusProperty {
            return DamageBonusProperty(ability, data.asInt)
        }
        override fun getKey(): String = "damage_bonus"
    }

    fun getDamageBonus(): Int = bonus

    protected open fun getDamageBonusLabel(): Text? = null

    fun getDamageBonusRate(): Double = getDamageBonus() / 100.0

    override fun getTooltip(): List<Text> {
        val color = if (bonus < 0) Formatting.RED else Formatting.WHITE
        val value = LiteralText("${signed(bonus)}%").formatted(color)
        getDamageBonusLabel()?.let {
            value.append(it)
        }
        return listOf(Symbol.DAMAGE.asText().append(" ")
            .append(Translations.TOOLTIP_ABILITY_DAMAGE_BONUS.formatted(Formatting.GRAY).append(": "))
            .append(value))
    }

    class Raw(ability: Ability, bonus: Int): DamageBonusProperty(ability, bonus) {
        companion object: Type<Raw> {
            override fun create(ability: Ability, data: JsonElement): Raw {
                return Raw(ability, data.asInt)
            }
            override fun getKey(): String = "raw_damage_bonus"
        }

        override fun getDamageBonusLabel(): Text? {
            return LiteralText(" (").formatted(Formatting.GRAY)
                .append(Translations.TOOLTIP_ABILITY_BONUS_DAMAGE_RAW.formatted(Formatting.GRAY))
                .append(LiteralText(")").formatted(Formatting.GRAY))
        }
    }

    class PerFocus(ability: Ability, bonus: Int):
        DamageBonusProperty(ability, bonus), SetupProperty, ModifiableProperty {
        companion object: Type<PerFocus> {
            override fun create(ability: Ability, data: JsonElement): PerFocus {
                return PerFocus(ability, data.asInt)
            }
            override fun getKey(): String = "focus_damage_bonus"
        }

        override fun getDamageBonusLabel(): Text? {
            return LiteralText(" (").formatted(Formatting.DARK_GRAY)
                .append(Translations.TOOLTIP_ABILITY_BONUS_DAMAGE_FOCUS.formatted(Formatting.DARK_GRAY))
                .append(LiteralText(")").formatted(Formatting.DARK_GRAY))
        }

        override fun modify(entry: PropertyEntry) {
            from(entry)?.let {
                val upgrade = it.getDamageBonus() + getDamageBonus()
                entry.setProperty(getKey(), PerFocus(it.getAbility(), upgrade))
            }
        }

        override fun setup(entry: PropertyEntry) {
            entry.setProperty(getKey(), this)
        }
    }
}