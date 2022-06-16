package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.Skill
import io.github.nbcss.wynnlib.data.Tier
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

    fun getLevel(): IRange

    fun getType(): EquipmentType

    fun getRequirement(skill: Skill): Int

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