package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.Keyed
import java.util.*
import kotlin.collections.LinkedHashMap

enum class Restriction(private val id: String): Keyed{
    QUEST_ITEM("QUEST ITEM"),
    UNTRADABLE("UNTRADABLE"),
    SOULBOUND("SOULBOUND");

    companion object {
        private val VALUE_MAP: MutableMap<String, Restriction> = LinkedHashMap()
        init {
            values().forEach { VALUE_MAP[it.id.uppercase(Locale.getDefault())] = it }
        }

        fun fromId(id: String): Restriction? {
            return VALUE_MAP[id.uppercase(Locale.getDefault())]
        }
    }

    override fun getKey(): String = name
}