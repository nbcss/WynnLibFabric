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
            values().forEach { VALUE_MAP[it.name.lowercase(Locale.getDefault())] = it }
        }

        fun getAttackSpeed(id: String): AttackSpeed {
            return VALUE_MAP.getOrDefault(id.lowercase(Locale.getDefault()), NORMAL)
        }
    }

    override fun getKey(): String = name

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.attack_speed.${getKey().lowercase(Locale.getDefault())}"
    }
}
