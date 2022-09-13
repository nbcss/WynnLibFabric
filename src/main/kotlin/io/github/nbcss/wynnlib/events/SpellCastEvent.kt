package io.github.nbcss.wynnlib.events

import io.github.nbcss.wynnlib.abilities.Ability

class SpellCastEvent(val ability: Ability,
                     val cost: Int) {
    companion object: EventHandler.HandlerList<SpellCastEvent>()
}