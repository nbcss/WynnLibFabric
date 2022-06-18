package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.lang.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import java.util.*

enum class Element(val displayName: String,
                   val damageName: String,
                   val defenceName: String,
                   val damageBonusName: String,
                   val defenceBonusName: String
                   ): Keyed, Translatable {
    FIRE("§c✹ Fire",
        "fireDamage",
        "fireDefense",
        "bonusFireDamage",
        "bonusFireDefense"),
    WATER("§b❉ Water",
        "waterDamage",
        "waterDefense",
        "bonusWaterDamage",
        "bonusWaterDefense"),
    AIR("§f❋ Air",
        "airDamage",
        "airDefense",
        "bonusAirDamage",
        "bonusAirDefense"),
    THUNDER("§e✦ Thunder",
        "thunderDamage",
        "thunderDefense",
        "bonusThunderDamage",
        "bonusThunderDefense"),
    EARTH("§2✤ Earth",
        "earthDamage",
        "earthDefense",
        "bonusEarthDamage",
        "bonusEarthDefense");

    companion object {
        private val VALUE_MAP: MutableMap<String, Element> = LinkedHashMap()
        init {
            values().forEach { VALUE_MAP[it.name.uppercase(Locale.getDefault())] = it }
        }

        fun fromId(id: String): Element? {
            return VALUE_MAP[id.uppercase(Locale.getDefault())]
        }
    }

    override fun getKey(): String = name

    override fun getTranslationKey(label: String?): String {
        val key = getKey().lowercase(Locale.getDefault())
        if ("tooltip.damage" == label){
            return "wynnlib.tooltip.${key}_damage"
        }else if("tooltip.defence" == label){
            return "wynnlib.tooltip.${key}_defence"
        }
        return "wynnlib.element.$key"
    }
}