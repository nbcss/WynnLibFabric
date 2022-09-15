package io.github.nbcss.wynnlib.items.equipments

import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.PowderSpecial
import io.github.nbcss.wynnlib.data.Skill

interface RolledEquipment: GearEquipment {
    fun meetLevelReq(): Boolean
    fun meetClassReq(): Boolean
    fun meetQuestReq(): Boolean
    fun meetSkillReq(skill: Skill): Boolean
    fun getPowderSpecial(): PowderSpecial?
    fun getPowders(): List<Element>
    fun getRoll(): Int
    fun getIdentificationValue(id: Identification): Int
    fun getIdentificationStars(id: Identification): Int
}