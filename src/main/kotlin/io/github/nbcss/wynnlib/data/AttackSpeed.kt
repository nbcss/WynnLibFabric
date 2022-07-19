package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import java.util.*

enum class AttackSpeed(val displayName: String,
                       val speedModifier: Double): Keyed, Translatable {
    SUPER_SLOW("Super Slow Attack Speed", 0.51),
    VERY_SLOW("Very Slow Attack Speed", 0.83),
    SLOW("Slow Attack Speed", 1.5),
    NORMAL("Normal Attack Speed", 2.05),
    FAST("Fast Attack Speed", 2.5),
    VERY_FAST("Very Fast Attack Speed", 3.1),
    SUPER_FAST("Super Fast Attack Speed", 4.3);

    companion object {
        private val NAME_MAP: Map<String, AttackSpeed> = mapOf(
            pairs = values().map { it.name.uppercase() to it }.toTypedArray()
        )
        private val DISPLAY_NAME_MAP: Map<String, AttackSpeed> = mapOf(
            pairs = values().map { it.displayName to it }.toTypedArray()
        )

        /**
         * Get AttackSpeed instance from case-insensitive name.
         *
         * @param name the id of the attack speed.
         * @return AttackSpeed instance for associated name;
         * if there is not a such instance with given name, the method will return Normal attack speed.
         */
        fun fromName(name: String): AttackSpeed {
            return NAME_MAP.getOrDefault(name.uppercase(), NORMAL)
        }

        fun fromDisplayName(displayName: String): AttackSpeed? {
            return DISPLAY_NAME_MAP[displayName]
        }
    }

    override fun getKey(): String = name

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.attack_speed.${getKey().lowercase(Locale.getDefault())}"
    }
}
