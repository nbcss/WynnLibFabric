package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.Keyed

enum class TomeType(
    val id: String,                     // The id of the tome.
    val displayName: String,            // The display name of the tome; using to match type.
    val effect: String?,                // The effect_key of the tome. TODO Specific name is not decided yet.
    val restriction: Restriction,       // The restriction of the tome.
) : Keyed {
    SLAYING("slaying", "Slaying", "slaying_xp", Restriction.SOULBOUND),
    GATHERING("gathering", "Gathering", "gathering_xp", Restriction.SOULBOUND),
    DUNGEONEERING("dungeoneering", "Dungeoneering", "dungeon_xp", Restriction.SOULBOUND),
    WEAPON("weapon", "Weapon", "damage_to_mobs", Restriction.SOULBOUND),
    ARMOUR("armour", "Armour", "damage_from_mobs", Restriction.SOULBOUND),
    GUILD("guild", "Guild", null, Restriction.UNTRADABLE), ;

    override fun getKey(): String = id

    fun getEffect(): Identification? = effect?.let { Identification.fromName(it) }

    companion object {
        private val ID_MAP: Map<String, TomeType> = mapOf(
            pairs = values().map { it.id to it }.toTypedArray()
        )

        fun fromId(id: String): TomeType =
            ID_MAP[id.lowercase()] ?: throw IllegalArgumentException("Unknown tome type id: $id")
    }

}