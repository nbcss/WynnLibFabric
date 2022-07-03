package io.github.nbcss.wynnlib.abilities.effects.archer

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.display.ArcherStreamTooltip
import io.github.nbcss.wynnlib.abilities.display.DamageTooltip
import io.github.nbcss.wynnlib.abilities.display.EffectTooltip
import io.github.nbcss.wynnlib.abilities.effects.AbilityEffect
import io.github.nbcss.wynnlib.abilities.effects.SpellDamageModifier
import io.github.nbcss.wynnlib.abilities.properties.ArcherStreamProperty

class WindStorm(parent: Ability, json: JsonObject): SpellDamageModifier(parent, json),
    ArcherStreamProperty {
    companion object: AbilityEffect.Factory {
        override fun create(parent: Ability, properties: JsonObject): WindStorm {
            return WindStorm(parent, properties)
        }
    }
    private val streams: Int = ArcherStreamProperty.read(json)

    override fun getArcherStreams(): Int = streams

    override fun getTooltipItems(): List<EffectTooltip> {
        return listOf(DamageTooltip.Modifier, ArcherStreamTooltip.Modifier)
    }
}