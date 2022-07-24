package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import java.util.*

enum class Restriction(private val id: String,
                       private val displayName: String): Keyed, Translatable {
    QUEST_ITEM("QUEST ITEM", "Quest Item"),
    UNTRADABLE("UNTRADABLE", "Untradable Item"),
    SOULBOUND("SOULBOUND", "Soulbound Item");

    companion object {
        private val ID_MAP: Map<String, Restriction> = mapOf(
            pairs = values().map { it.id.uppercase() to it }.toTypedArray()
        )
        private val NAME_MAP: Map<String, Restriction> = mapOf(
            pairs = values().map { it.displayName to it }.toTypedArray()
        )

        fun fromId(id: String): Restriction? {
            return ID_MAP[id.uppercase()]
        }

        fun fromDisplayName(name: String): Restriction? {
            return NAME_MAP[name]
        }
    }

    override fun getKey(): String = name

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.restriction.${getKey().lowercase(Locale.getDefault())}"
    }
}