package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.Keyed

enum class CraftedType(private val id: String,
                       private val profession: Profession,
                       private val translationKey: String): Keyed, Translatable {
    SPEAR("SPEAR", Profession.WEAPONSMITHING, "wynnlib.item_type.spear"),
    BOW("BOW", Profession.WOODWORKING, "wynnlib.item_type.bow"),
    WAND("WAND", Profession.WOODWORKING, "wynnlib.item_type.wand"),
    DAGGER("DAGGER", Profession.WEAPONSMITHING, "wynnlib.item_type.dagger"),
    RELIK("RELIK", Profession.WOODWORKING, "wynnlib.item_type.relik"),
    HELMET("HELMET", Profession.ARMOURING, "wynnlib.item_type.helmet"),
    CHESTPLATE("CHESTPLATE", Profession.ARMOURING, "wynnlib.item_type.chestplate"),
    PANTS("PANTS", Profession.TAILORING, "wynnlib.item_type.leggings"),
    BOOTS("BOOTS", Profession.TAILORING, "wynnlib.item_type.boots"),
    RING("RING", Profession.JEWELING, "wynnlib.item_type.ring"),
    BRACELET("BRACELET", Profession.JEWELING, "wynnlib.item_type.bracelet"),
    NECKLACE("NECKLACE", Profession.JEWELING, "wynnlib.item_type.necklace"),
    POTION("POTION", Profession.ALCHEMISM, "wynnlib.consumable_type.crafted_potion"),
    FOOD("FOOD", Profession.COOKING, "wynnlib.consumable_type.crafted_food"),
    SCROLL("SCROLL", Profession.SCRIBING, "wynnlib.consumable_type.crafted_scroll");

    companion object {
        private val ID_MAP: Map<String, CraftedType> = mapOf(
            pairs = values().map { it.id.uppercase() to it }.toTypedArray()
        )
        fun fromId(id: String): CraftedType? {
            return ID_MAP[id.uppercase()]
        }
    }

    override fun getTranslationKey(label: String?): String = translationKey

    override fun getKey(): String = id

    fun getProfession(): Profession = profession
}