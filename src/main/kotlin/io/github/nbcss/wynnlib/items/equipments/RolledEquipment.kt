package io.github.nbcss.wynnlib.items.equipments

import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.PowderSpecial

interface RolledEquipment: Equipment {
    fun getPowderSpecial(): PowderSpecial?
    fun getRoll(): Int
    fun getIdentificationValue(id: Identification): Int
}