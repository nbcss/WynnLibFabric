package io.github.nbcss.wynnlib.abilities.properties

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Element

interface DamageProperty {
    companion object {
        const val HITS_KEY: String = "hits"
        const val NEUTRAL_DAMAGE_KEY: String = "neutral_damage"
        fun read(data: JsonObject): Damage = Damage(data)
    }

    fun getDamage(): Damage

    data class Damage(private val hits: Int,
                      private val neutralDamage: Int,
                      private val elementalDamage: Map<Element, Int>) {
        constructor(json: JsonObject): this(
            if (json.has(HITS_KEY)) json[HITS_KEY].asInt else 1,
            if (json.has(NEUTRAL_DAMAGE_KEY)) json[NEUTRAL_DAMAGE_KEY].asInt else 0,
            mapOf(pairs = Element.values().map {
                val key = "${it.getKey().lowercase()}_damage"
                it to if (json.has(key)) json[key].asInt else 0
            }.toTypedArray())
        )

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
    }
}