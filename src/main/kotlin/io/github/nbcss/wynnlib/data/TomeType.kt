package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.Keyed

enum class TomeType(
    val id: String,                     // The id of the tome.
    val displayName: String,            // The display name of the tome; using to match type.
) : Keyed {
    SLAYING("slaying", "Slaying"),
    GATHERING("gathering", "Gathering"),
    DUNGEONEERING("dungeoneering", "Dungeoneering"),
    WEAPON("weapon", "Weapon"),
    ARMOUR("armour", "Armour"),
    GUILD("guild", "Guild");

    override fun getKey(): String = id

    //fun getEffect(): Identification? = effect?.let { Identification.fromApiKey(it) }

    companion object {
        private val ID_MAP: Map<String, TomeType> = mapOf(
            pairs = values().map { it.id to it }.toTypedArray()
        )

        fun fromId(id: String): TomeType =
            ID_MAP[id.lowercase()] ?: throw IllegalArgumentException("Unknown tome type id: $id")
    }
}