package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.utils.Keyed
import java.util.*

enum class CharacterClass(private val weaponName: String): Keyed {
    WARRIOR("Spear"),
    ARCHER("Bow"),
    MAGE("Wand"),
    ASSASSIN("Dagger"),
    SHAMAN("Relik");
    companion object {
        private val VALUE_MAP: MutableMap<EquipmentType, CharacterClass> = EnumMap(EquipmentType::class.java)
        init {
            values().forEach { VALUE_MAP[EquipmentType.getEquipmentType(it.weaponName)] = it }
        }

        fun fromWeaponType(type: EquipmentType): CharacterClass? = VALUE_MAP[type]
    }

    override fun getKey(): String = name
}