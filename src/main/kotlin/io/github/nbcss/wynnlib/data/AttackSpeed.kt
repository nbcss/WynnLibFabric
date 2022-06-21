package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.lang.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import java.util.*
import kotlin.collections.LinkedHashMap

enum class AttackSpeed(val displayName: String,
                       val speedModifier: Double): Keyed, Translatable {
    SUPER_SLOW("Super Slow", 0.51),
    VERY_SLOW("Very Slow", 0.83),
    SLOW("Slow", 1.5),
    NORMAL("Normal", 2.05),
    FAST("Fast", 2.5),
    VERY_FAST("Very Fast", 3.1),
    SUPER_FAST("Super Fast", 4.3);

    companion object {
        private val VALUE_MAP: MutableMap<String, AttackSpeed> = LinkedHashMap()
        init {
            values().forEach { VALUE_MAP[it.name.lowercase()] = it }
        }

        /**
         * Get AttackSpeed instance from case-insensitive name.
         *
         * @param name the id of the attack speed.
         * @return AttackSpeed instance for associated name;
         * if there is not a such instance with given name, the method will return Normal attack speed.
         */
        fun fromName(name: String): AttackSpeed {
            return VALUE_MAP.getOrDefault(name.lowercase(), NORMAL)
        }
    }

    override fun getKey(): String = name

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.attack_speed.${getKey().lowercase(Locale.getDefault())}"
    }
}
