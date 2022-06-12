package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.Keyed

interface Equipment : Keyed {
    fun getDisplayName(): String
    fun getTier(): Tier
    fun getIdentification(id: Identification): IntRange

    /**
     * Convert the Equipment to a Weapon instance.
     *
     * @return converted Weapon instance, or null if the Equipment is not a weapon.
     */
    fun asWeapon(): Weapon
}