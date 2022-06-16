package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.Keyed

enum class Element(val displayName: String,
                   val damageName: String,
                   val defenceName: String,
                   val damageBonusName: String,
                   val defenceBonusName: String): Keyed {
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

    override fun getKey(): String = name
}