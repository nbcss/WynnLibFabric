package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.abilities.properties.general.DamageModifierProperty
import io.github.nbcss.wynnlib.i18n.Translatable

data class DamageMultiplier(private val hits: Int,
                            private val damageLabel: Label?,
                            private val neutralDamage: Int,
                            private val elementalDamage: Map<Element, Int>) {
    companion object {
        private const val HITS_KEY: String = "hits"
        private const val DAMAGE_LABEL_KEY: String = "label"
        private const val NEUTRAL_DAMAGE_KEY: String = "neutral"
        fun fromJson(data: JsonObject, defaultHits: Int = 1): DamageMultiplier {
            val hits = if (data.has(HITS_KEY)) data[HITS_KEY].asInt else defaultHits
            val label = if (data.has(DAMAGE_LABEL_KEY)) Label.fromName(data[DAMAGE_LABEL_KEY].asString) else null
            val neutral = if (data.has(NEUTRAL_DAMAGE_KEY)) data[NEUTRAL_DAMAGE_KEY].asInt else 0
            val elementalDamage = mapOf(pairs = Element.values().map {
                val key = it.getKey().lowercase()
                it to if (data.has(key)) data[key].asInt else 0
            }.toTypedArray())
            return DamageMultiplier(hits, label,neutral, elementalDamage)
        }
    }

    fun getHits(): Int = hits

    fun getDamageLabel(): Label? = damageLabel

    fun isZero(): Boolean {
        return getNeutralDamage() == 0 && Element.values().all { getElementalDamage(it) == 0 }
    }

    fun getTotalDamage(): Int {
        return getNeutralDamage() + Element.values().sumOf { getElementalDamage(it) }
    }

    fun getNeutralDamage(): Int = neutralDamage

    fun getElementalDamage(element: Element): Int = elementalDamage[element] ?: 0

    fun getNeutralDamageRate(): Double = neutralDamage / 100.0

    fun getElementalDamageRate(element: Element): Double = getElementalDamage(element) / 100.0

    fun add(damage: DamageMultiplier, label: Label? = this.damageLabel): DamageMultiplier {
        val hits = this.hits + damage.hits
        val neutral = this.neutralDamage + damage.neutralDamage
        val elementalDamage = mapOf(pairs = Element.values().map {
            it to (this.getElementalDamage(it) + damage.getElementalDamage(it))
        }.toTypedArray())
        return DamageMultiplier(hits, label, neutral, elementalDamage)
    }

    enum class Label: Translatable {
        ATTACK,
        BASH,
        MANTLE_LOST,
        RIPPLE,
        ARROW,
        HIT,
        SHRAPNEL,
        STRIKE,
        SHURIKEN,
        WINDED;
        companion object {
            private val VALUE_MAP: Map<String, Label> = mapOf(
                pairs = values().map { it.name.uppercase() to it }.toTypedArray())
            fun fromName(name: String): Label? = VALUE_MAP[name.uppercase()]
        }

        override fun getTranslationKey(label: String?): String {
            return "wynnlib.tooltip.ability.damage_label.${name.lowercase()}"
        }
    }
}