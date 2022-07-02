package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Element

interface DamageProperty {
    companion object {
        private const val HITS_KEY: String = "hits"
        private const val NEUTRAL_DAMAGE_KEY: String = "neutral_damage"
        fun readModifier(data: JsonObject): Damage {
            return asDamage(if (data.has(HITS_KEY)) data[HITS_KEY].asInt else 0, data)
        }

        fun readDamage(data: JsonObject): Damage {
            return asDamage(if (data.has(HITS_KEY)) data[HITS_KEY].asInt else 1, data)
        }

        private fun asDamage(hits: Int, data: JsonObject): Damage {
            val neutral = if (data.has(NEUTRAL_DAMAGE_KEY)) data[NEUTRAL_DAMAGE_KEY].asInt else 0
            val elementalDamage = mapOf(pairs = Element.values().map {
                val key = "${it.getKey().lowercase()}_damage"
                it to if (data.has(key)) data[key].asInt else 0
            }.toTypedArray())
            return Damage(hits, neutral, elementalDamage)
        }
    }

    fun getDamage(): Damage

    data class Damage(private val hits: Int,
                      private val neutralDamage: Int,
                      private val elementalDamage: Map<Element, Int>) {

        fun getHits(): Int = hits

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

        fun add(damage: Damage): Damage {
            val hits = this.hits + damage.hits
            val neutral = this.neutralDamage + damage.neutralDamage
            val elementalDamage = mapOf(pairs = Element.values().map {
                it to (this.getElementalDamage(it) + damage.getElementalDamage(it))
            }.toTypedArray())
            return Damage(hits, neutral, elementalDamage)
        }
    }
}