package io.github.nbcss.wynnlib.abilities.effects.warrior

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.DamageTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.BaseEffect
import io.github.nbcss.wynnlib.abilities.properties.legacy.DamageProperty

class Counter(parent: Ability, json: JsonObject): BaseEffect(parent, json),
    DamageProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): Counter {
            return Counter(parent, properties)
        }
    }
    private val damage: DamageProperty.Damage = DamageProperty.readDamage(json)

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip)
    }
}