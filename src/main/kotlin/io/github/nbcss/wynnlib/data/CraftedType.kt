package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.Keyed

enum class CraftedType(private val id: String): Keyed {
    SPEAR("SPEAR"),
    BOW("BOW"),
    WAND("WAND"),
    DAGGER("DAGGER"),
    RELIK("RELIK"),
    HELMET("HELMET"),
    CHESTPLATE("CHESTPLATE"),
    PANTS("PANTS"),
    BOOTS("BOOTS"),
    RING("RING"),
    BRACELET("BRACELET"),
    NECKLACE("NECKLACE"),
    POTION("POTION"),
    FOOD("FOOD"),
    SCROLL("SCROLL");
    companion object {
        private val ID_MAP: Map<String, CraftedType> = mapOf(
            pairs = values().map { it.id.uppercase() to it }.toTypedArray()
        )
        fun fromId(id: String): CraftedType? {
            return ID_MAP[id.uppercase()]
        }
    }

    override fun getKey(): String = id
}