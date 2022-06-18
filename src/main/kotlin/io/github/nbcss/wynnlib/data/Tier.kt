package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.lang.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.util.Formatting
import java.util.*
import kotlin.collections.LinkedHashMap
import kotlin.math.ceil

enum class Tier(val id: String,
                val formatting: Formatting,
                val displayName: String,
                private val priceBase: Double?,
                private val priceCoefficient: Double?
                ): Keyed, Translatable {
    MYTHIC("Mythic", Formatting.DARK_PURPLE, "Mythic Item", 90.0, 18.0),
    FABLED("Fabled", Formatting.RED, "Fabled Item", 16.0, 8.0),
    LEGENDARY("Legendary", Formatting.AQUA, "Legendary Item", 12.0, 4.5),
    RARE("Rare", Formatting.LIGHT_PURPLE, "Rare Item", 8.0, 1.2),
    UNIQUE("Unique", Formatting.YELLOW, "Unique Item", 3.0, 0.5),
    SET("Set", Formatting.GREEN, "Set Item", 8.0, 1.5),
    NORMAL("Normal", Formatting.WHITE, "Normal Item", null, null),
    CRAFTED("Crafted", Formatting.DARK_AQUA, "Crafted Item", null, null);

    companion object {
        private val VALUE_MAP: MutableMap<String, Tier> = LinkedHashMap()
        init {
            values().forEach { VALUE_MAP[it.name.lowercase(Locale.getDefault())] = it }
        }

        fun getTier(tierName: String): Tier {
            return VALUE_MAP.getOrDefault(tierName.lowercase(Locale.getDefault()), NORMAL)
        }
    }

    override fun getKey(): String = name

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.tier.${getKey().lowercase(Locale.getDefault())}"
    }

    fun canIdentify(): Boolean {
        return priceBase != null && priceCoefficient != null
    }

    fun getIdentifyPrice(level: Int): Int {
        return if (canIdentify()) ceil(priceBase!! + level * priceCoefficient!!).toInt() else 0
    }
}
