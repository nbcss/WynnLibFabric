package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.util.Formatting
import java.util.*
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
        private val NAME_MAP: Map<String, Tier> = mapOf(
            pairs = values().map { it.name.uppercase() to it }.toTypedArray()
        )
        private val ID_MAP: Map<String, Tier> = mapOf(
            pairs = values().map { it.id to it }.toTypedArray()
        )

        fun fromName(name: String): Tier {
            return NAME_MAP.getOrDefault(name.uppercase(), NORMAL)
        }

        fun fromId(id: String): Tier? {
            return ID_MAP[id]
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
