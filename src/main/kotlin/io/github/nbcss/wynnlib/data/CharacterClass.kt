package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.lang.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import java.util.*

enum class CharacterClass(private val weaponName: String,
                          private val mainKey: MouseKey): Keyed, Translatable {
    WARRIOR("Spear", MouseKey.LEFT),
    ARCHER("Bow", MouseKey.RIGHT),
    MAGE("Wand", MouseKey.LEFT),
    ASSASSIN("Dagger", MouseKey.LEFT),
    SHAMAN("Relik", MouseKey.LEFT);
    companion object {
        private val VALUE_MAP: MutableMap<EquipmentType, CharacterClass> = EnumMap(EquipmentType::class.java)
        init {
            values().forEach { VALUE_MAP[it.getWeapon()] = it }
        }

        fun fromWeaponType(type: EquipmentType): CharacterClass? = VALUE_MAP[type]
    }

    fun getMainAttackKey(): MouseKey = mainKey

    fun getSpellKey(): MouseKey = mainKey.opposite()

    fun getWeapon(): EquipmentType = EquipmentType.getEquipmentType(weaponName)

    override fun getKey(): String = name

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.class.${getKey().lowercase(Locale.getDefault())}"
    }
}