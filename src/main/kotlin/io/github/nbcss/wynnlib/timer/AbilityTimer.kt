package io.github.nbcss.wynnlib.timer

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.registry.AbilityRegistry
import net.minecraft.util.Identifier
import org.apache.commons.lang3.StringEscapeUtils
import kotlin.math.max

class AbilityTimer(private val ability: Ability,
                   private val texture: Identifier,
                   private val cooldown: Boolean,
                   private val duration: Long,
                   startTime: Long): IconTimer {
    companion object {
        fun matches(entry: FooterEntry, startTime: Long): AbilityTimer? {
            if (entry.duration == null)
                return null
            val abilities = AbilityRegistry.fromDisplayName(entry.name)
            if (abilities.isNotEmpty()){
                val ability = abilities.first()
                ability.getMetadata()?.let {
                    val texture = it.getTexture()
                    val duration = entry.duration.plus(1).times(20).toLong()
                    val cooldown = entry.icon == "\u00A78\u2B24"
                    return AbilityTimer(ability, texture, cooldown, duration, startTime)
                }
            }
            return null
        }
    }
    private var expired: Boolean = false
    private var currentTime: Long = startTime
    private val endTime: Long = startTime + duration

    override fun getIcon(): Identifier {
        return texture
    }

    override fun isCooldown(): Boolean {
        return cooldown
    }

    override fun isExpired(): Boolean = expired

    override fun updateWorldTime(time: Long) {
        currentTime = time
        if (currentTime > endTime)
            expired = true
    }

    override fun getDuration(): Double {
        val time = (endTime - currentTime) / 20.0
        return max(0.0, time)
    }

    override fun getFullDuration(): Double {
        return duration / 20.0
    }

    override fun getKey(): String {
        return ability.getKey()
    }

    override fun asIconTimer(): IconTimer {
        return this
    }
}