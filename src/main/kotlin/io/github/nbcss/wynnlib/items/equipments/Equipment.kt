package io.github.nbcss.wynnlib.items.equipments

import io.github.nbcss.wynnlib.data.*
import io.github.nbcss.wynnlib.items.BaseItem
import io.github.nbcss.wynnlib.items.IdentificationHolder
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.Keyed

interface Equipment : Keyed, BaseItem, IdentificationHolder {
    /**
     * Get the tier of the equipment.
     *
     * @return item tier
     */
    fun getTier(): Tier

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
     * Get restriction of the equipment.
     *
     * @return restriction
     */
    fun getRestriction(): Restriction?

    /**
     * Check whether the item can be rerolled.
     */
    fun isIdentifiable(): Boolean
}