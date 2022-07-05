package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.DamageBonusTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.IDTransferBooster
import io.github.nbcss.wynnlib.abilities.properties.legacy.DamageBonusProperty
import io.github.nbcss.wynnlib.i18n.Translations
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class Vehement(parent: Ability, json: JsonObject): IDTransferBooster(parent, json),
    DamageBonusProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): Vehement {
            return Vehement(parent, properties)
        }
    }
    private val bonus: Int = DamageBonusProperty.read(json)

    override fun getDamageBonus(): Int = bonus

    override fun getDamageBonusLabel(): Text {
        return LiteralText(" (").formatted(Formatting.GRAY)
            .append(Translations.TOOLTIP_ABILITY_BONUS_DAMAGE_RAW.formatted(Formatting.GRAY))
            .append(LiteralText(")").formatted(Formatting.GRAY))
    }

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageBonusTooltip)
    }
}