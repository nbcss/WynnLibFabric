package io.github.nbcss.wynnlib.items.equipments

import io.github.nbcss.wynnlib.items.equipments.Equipment
import io.github.nbcss.wynnlib.items.equipments.Weapon
import io.github.nbcss.wynnlib.items.equipments.Wearable

interface GearEquipment: Equipment {

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