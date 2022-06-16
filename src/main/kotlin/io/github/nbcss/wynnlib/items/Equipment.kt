package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.data.*
import io.github.nbcss.wynnlib.utils.BaseItem
import io.github.nbcss.wynnlib.utils.IRange
import io.github.nbcss.wynnlib.utils.Keyed

interface Equipment : Keyed, BaseItem {
    /**
     * Get the tier of the equipment.
     *
     * @return item tier
     */
    fun getTier(): Tier

    /**
     * Get the roll range of the given identification.
     *
     * @param id: the identification to query
     * @return the range of the given identification
     */
    fun getIdentification(id: Identification): IRange

    /**
     * Get the type of the Equipment (e.g. Helmet, Bow...)
     *
     * @return equipment type
     */
    fun getType(): EquipmentType

    /**
     * Get the level (range) requirement of the equipment.
     * Note that regular items will always have constant level req.
     *
     * @return level req range
     */
    fun getLevel(): IRange

    /**
     * Get the class req of the equipment.
     * Note that at the moment only weapon have class req.
     *
     * @return class req, or null if the equipment don't have class req
     */
    fun getClassReq(): CharacterClass?

    /**
     * Get the quest req of the equipment.
     *
     * @return quest req, or null if the equipment don't have quest req
     */
    fun getQuestReq(): String?

    /**
     * Get the skill point req of the equipment.
     *
     * @param skill skill type for check
     * @return the skill point req for the given skill
     */
    fun getRequirement(skill: Skill): Int

    /**
     * Get the number of powder slots of the equipment.
     *
     * @return powder slot num
     */
    fun getPowderSlot(): Int

    /**
     * Convert the Equipment to a Weapon instance.
     *
     * @return converted Weapon instance, or null if the Equipment is not a weapon.
     */
    fun asWeapon(): Weapon?

    /**
     * Convert the Equipment to a Wearable instance.
     *
     * @return converted Wearable instance, or null if the Equipment is not wearable.
     */
    fun asWearable(): Wearable?
}