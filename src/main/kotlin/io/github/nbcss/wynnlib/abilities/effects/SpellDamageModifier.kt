package io.github.nbcss.wynnlib.abilities.effects

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.DamageTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.properties.legacy.DamageProperty

open class SpellDamageModifier(parent: Ability, json: JsonObject): BoundSpellEffect(parent, json),
    DamageProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): SpellDamageModifier {
            return SpellDamageModifier(parent, properties)
        }
    }
    private val damage: DamageProperty.Damage = DamageProperty.readModifier(json)

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip.Modifier)
    }
}