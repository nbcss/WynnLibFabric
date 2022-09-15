package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.Keyed

enum class TomeType(
    val id: String,                     // The id of the tome.
    val displayName: String,            // The display name of the tome; using to match type.
    val effect: String?,                // The effect_key of the tome.
    val restriction: Restriction,       // The restriction of the tome.
) : Keyed {
    SLAYING("slaying", "Slaying", "slayingXp", Restriction.SOULBOUND),
    GATHERING("gathering", "Gathering", "gatheringXp", Restriction.SOULBOUND),
    DUNGEONEERING("dungeoneering", "Dungeoneering", "dungeonXp", Restriction.SOULBOUND),
    WEAPON("weapon", "Weapon", "damageToMobs", Restriction.SOULBOUND),
    ARMOUR("armour", "Armour", "defenceToMobs", Restriction.SOULBOUND),
    GUILD("guild", "Guild", null, Restriction.UNTRADABLE), ;

    override fun getKey(): String = id

    fun getEffect(): Identification? = effect?.let { Identification.fromApiKey(it) }

    companion object {
        private val ID_MAP: Map<String, TomeType> = mapOf(
            pairs = values().map { it.id to it }.toTypedArray()
        )

        fun fromId(id: String): TomeType =
            ID_MAP[id.lowercase()] ?: throw IllegalArgumentException("Unknown tome type id: $id")
    }

}