package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable

enum class IdentificationGroup: Translatable {
    SKILL_POINT_BONUS,
    COMBAT,
    SURVIVABILITY,
    ELEMENT_DAMAGE_BONUS,
    ELEMENT_DEFENCE_BONUS,
    MANEUVERABILITY,
    MISC,
    SPELL_COST,
    TOME_EFFECT,
    CHARM_EXCLUSIVE;
    companion object {
        private val NAME_MAP: Map<String, IdentificationGroup> = mapOf(
            pairs = values().map { it.name to it }.toTypedArray()
        )

        fun fromName(name: String): IdentificationGroup {
            return NAME_MAP.getOrDefault(name, MISC)
        }
    }

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.id_group.${name.lowercase()}"
    }
}