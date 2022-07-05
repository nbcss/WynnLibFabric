package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.display.MainAttackDamageModifierTooltip
import io.github.nbcss.wynnlib.abilities.display.MainAttackRangeModifierTooltip
import io.github.nbcss.wynnlib.abilities.properties.legacy.MainAttackDamageModifierProperty
import io.github.nbcss.wynnlib.abilities.properties.legacy.MainAttackRangeModifierProperty

class WeaponProficiency(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    MainAttackDamageModifierProperty, MainAttackRangeModifierProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): WeaponProficiency {
            return WeaponProficiency(parent, properties)
        }
    }
    private val damage: Int = MainAttackDamageModifierProperty.read(json)
    private val range: Double = MainAttackRangeModifierProperty.read(json)

    override fun getMainAttackDamageModifier(): Int = damage

    override fun getMainAttackRangeModifier(): Double = range

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(MainAttackDamageModifierTooltip, MainAttackRangeModifierTooltip)
    }
}