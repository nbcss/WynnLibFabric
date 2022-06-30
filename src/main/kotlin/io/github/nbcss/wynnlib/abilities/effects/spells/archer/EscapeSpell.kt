package io.github.nbcss.wynnlib.abilities.effects.spells.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.display.DamageTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.display.ManaCostTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.spells.SpellUnlock
import io.github.nbcss.wynnlib.abilities.properties.DamageProperty

class EscapeSpell(json: JsonObject): SpellUnlock(json), DamageProperty {
    companion object: AbilityEffect.Factory {
        override fun create(properties: JsonObject): EscapeSpell {
            return EscapeSpell(properties)
        }
    }
    private val damage: DamageProperty.Damage = DamageProperty.read(json)

    override fun getDamage(): DamageProperty.Damage = damage

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(ManaCostTooltip, DamageTooltip)
    }
}