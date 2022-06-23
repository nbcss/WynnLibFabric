package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.util.Formatting
import java.util.*

enum class Element(val displayName: String,
                   val damageName: String,
                   val defenceName: String,
                   val damageBonusName: String,
                   val defenceBonusName: String,
                   val color: Formatting,
                   val altColor: Formatting
                   ): Keyed, Translatable {
    FIRE("✹ Fire",
        "fireDamage",
        "fireDefense",
        "bonusFireDamage",
        "bonusFireDefense",
        Formatting.RED, Formatting.DARK_RED),
    WATER("❉ Water",
        "waterDamage",
        "waterDefense",
        "bonusWaterDamage",
        "bonusWaterDefense",
        Formatting.AQUA, Formatting.DARK_AQUA),
    AIR("❋ Air",
        "airDamage",
        "airDefense",
        "bonusAirDamage",
        "bonusAirDefense",
        Formatting.WHITE, Formatting.GRAY),
    THUNDER("✦ Thunder",
        "thunderDamage",
        "thunderDefense",
        "bonusThunderDamage",
        "bonusThunderDefense",
        Formatting.YELLOW, Formatting.GOLD),
    EARTH("✤ Earth",
        "earthDamage",
        "earthDefense",
        "bonusEarthDamage",
        "bonusEarthDefense",
        Formatting.DARK_GREEN, Formatting.GREEN);

    companion object {
        private val VALUE_MAP: MutableMap<String, Element> = LinkedHashMap()
        init {
            values().forEach { VALUE_MAP[it.name.uppercase()] = it }
        }

        fun fromId(id: String): Element? {
            return VALUE_MAP[id.uppercase()]
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