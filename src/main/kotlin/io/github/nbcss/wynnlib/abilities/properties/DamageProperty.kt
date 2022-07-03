package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.i18n.Translatable

interface DamageProperty {
    companion object {
        private const val HITS_KEY: String = "hits"
        private const val DAMAGE_LABEL_KEY: String = "damage_name"
        private const val NEUTRAL_DAMAGE_KEY: String = "neutral_damage"
        fun readModifier(data: JsonObject): Damage {
            return asDamage(if (data.has(HITS_KEY)) data[HITS_KEY].asInt else 0, data)
        }

        fun readDamage(data: JsonObject): Damage {
            return asDamage(if (data.has(HITS_KEY)) data[HITS_KEY].asInt else 1, data)
        }

        private fun asDamage(hits: Int, data: JsonObject): Damage {
            val label = if (data.has(DAMAGE_LABEL_KEY)) DamageLabel.fromName(data[DAMAGE_LABEL_KEY].asString) else null
            val neutral = if (data.has(NEUTRAL_DAMAGE_KEY)) data[NEUTRAL_DAMAGE_KEY].asInt else 0
            val elementalDamage = mapOf(pairs = Element.values().map {
                val key = "${it.getKey().lowercase()}_damage"
                it to if (data.has(key)) data[key].asInt else 0
            }.toTypedArray())
            return Damage(hits, label, neutral, elementalDamage)
        }
    }

    fun getDamage(): Damage

    data class Damage(private val hits: Int,
                      private val damageLabel: DamageLabel?,
                      private val neutralDamage: Int,
                      private val elementalDamage: Map<Element, Int>) {

        fun getHits(): Int = hits

        fun getDamageLabel(): DamageLabel? = damageLabel

        fun isZero(): Boolean {
            return getNeutralDamage() == 0 && Element.values().all { getElementalDamage(it) == 0 }
        }

        fun getTotalDamage(): Int {
            return getNeutralDamage() + Element.values().sumOf { getElementalDamage(it) }
        }

        fun getNeutralDamage(): Int = neutralDamage

        fun getElementalDamage(element: Element): Int = elementalDamage[element]!!

        fun getNeutralDamageRate(): Double = neutralDamage / 100.0

        fun getElementalDamageRate(element: Element): Double = getElementalDamage(element) / 100.0

        fun add(damage: Damage, label: DamageLabel? = this.damageLabel): Damage {
            val hits = this.hits + damage.hits
            val neutral = this.neutralDamage + damage.neutralDamage
            val elementalDamage = mapOf(pairs = Element.values().map {
                it to (this.getElementalDamage(it) + damage.getElementalDamage(it))
            }.toTypedArray())
            return Damage(hits, label, neutral, elementalDamage)
        }
    }

    enum class DamageLabel: Translatable {
        ATTACK,
        BASH,
        MANTLE_LOST,
        RIPPLE,
        ARROW,
        HIT,
        FOCUS,
        SHRAPNEL;
        companion object {
            private val VALUE_MAP: Map<String, DamageLabel> = mapOf(
                pairs = values().map { it.name.uppercase() to it }.toTypedArray())
            fun fromName(name: String): DamageLabel? = VALUE_MAP[name.uppercase()]
        }

        override fun getTranslationKey(label: String?): String {
            return "wynnlib.tooltip.ability.damage_label.${name.lowercase()}"
        }
    }
}