package io.github.nbcss.wynnlib.timer

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import net.minecraft.util.Identifier
import org.apache.commons.lang3.StringEscapeUtils
import kotlin.math.abs
import kotlin.math.max

class AbilityTimer(private val ability: Ability,
                   private val texture: Identifier,
                   private val cooldown: Boolean,
                   private val maxDuration: Double,
                   entry: FooterEntry,
                   startTime: Long):
    AbstractFooterEntryTimer(entry, startTime), IconTimer {
    companion object {
        fun matches(entry: FooterEntry, startTime: Long): AbilityTimer? {
            if (entry.duration == null)
                return null
            val abilities = AbilityRegistry.fromDisplayName(entry.name)
            if (abilities.isNotEmpty()){
                val ability = abilities.first()
                ability.getMetadata()?.let {
                    val texture = it.getTexture()
                    val duration = entry.duration.plus(1).times(20).toDouble() / 20.0
                    val cooldown = entry.icon == "\u00A78\u2B24"
                    return AbilityTimer(ability, texture, cooldown, duration, entry, startTime)
                }
            }
            return null
        }
    }

    override fun getIcon(): Identifier {
        return texture
    }

    override fun isCooldown(): Boolean {
        return cooldown
    }

    override fun getFullDuration(): Double = maxDuration

    override fun getKey(): String {
        return ability.getKey()
    }

    override fun asIconTimer(): IconTimer {
        return this
    }
}