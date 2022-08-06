package io.github.nbcss.wynnlib.timer

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import net.minecraft.util.Identifier
import org.apache.commons.lang3.StringEscapeUtils

class AbilityTimer(private val key: String,
                   private val ability: Ability,
                   private val texture: Identifier,
                   private val cooldown: Boolean,
                   private val maxDuration: Double,
                   entry: StatusEntry,
                   startTime: Long):
    AbstractFooterEntryTimer(entry, startTime), IconTimer {
    companion object {
        fun matches(entry: StatusEntry, startTime: Long): AbilityTimer? {
            if (entry.duration == null)
                return null
            AbilityRegistry.fromStatusName(entry.name)?.let { ability ->
                ability.getMetadata()?.let {
                    val texture = it.getTexture()
                    val duration = entry.duration.plus(1).toDouble()
                    val cooldown = entry.icon == "\u00A78\u2B24"
                    val key = entry.icon + "@" + ability.getKey()
                    return AbilityTimer(key, ability, texture, cooldown, duration, entry, startTime)
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

    override fun getKey(): String = key

    override fun asIconTimer(): IconTimer {
        return this
    }
}