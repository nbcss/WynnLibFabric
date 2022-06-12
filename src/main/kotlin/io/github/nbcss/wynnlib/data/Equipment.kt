package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.BaseItem
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
    fun getIdentification(id: Identification): IntRange

    /**
     * Convert the Equipment to a Weapon instance.
     *
     * @return converted Weapon instance, or null if the Equipment is not a weapon.
     */
    fun asWeapon(): Weapon

    /**
     * Convert the Equipment to a Armour instance.
     *
     * @return converted Armour instance, or null if the Equipment is not an armour.
     */
    fun asArmour(): Armour
}