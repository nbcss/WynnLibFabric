package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.DamageBonusTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BaseEffect
import io.github.nbcss.wynnlib.abilities.properties.DamageBonusProperty
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ABILITY_BONUS_DAMAGE_FOCUS
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class Focus(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    DamageBonusProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): Focus {
            return Focus(parent, properties)
        }
    }
    private val bonus: Int = DamageBonusProperty.read(json)

    override fun getDamageBonus(): Int = bonus

    override fun getDamageBonusLabel(): Text {
        return LiteralText(" (").formatted(Formatting.DARK_GRAY)
            .append(TOOLTIP_ABILITY_BONUS_DAMAGE_FOCUS.formatted(Formatting.DARK_GRAY))
            .append(LiteralText(")").formatted(Formatting.DARK_GRAY))
    }

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageBonusTooltip)
    }
}