package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import java.util.*

enum class CharacterClass(private val displayName: String,
                          private val weaponName: String,
                          private val mainKey: MouseKey): Keyed, Translatable {
    WARRIOR("Warrior/Knight", "Spear", MouseKey.LEFT),
    ARCHER("Archer/Hunter", "Bow", MouseKey.RIGHT),
    MAGE("Mage/Dark Wizard", "Wand", MouseKey.LEFT),
    ASSASSIN("Assassin/Ninja", "Dagger", MouseKey.LEFT),
    SHAMAN("Shaman/Skyseer", "Relik", MouseKey.LEFT);
    companion object {
        private val TYPE_MAP: Map<EquipmentType, CharacterClass> = mapOf(
            pairs = values().map { it.getWeapon() to it }.toTypedArray()
        )
        private val ID_MAP: Map<String, CharacterClass> = mapOf(
            pairs = values().map { it.name.uppercase() to it }.toTypedArray()
        )
        private val NAME_MAP: Map<String, CharacterClass> = mapOf(
            pairs = values().map { it.displayName to it }.toTypedArray()
        )

        fun fromId(id: String): CharacterClass? = ID_MAP[id.uppercase()]

        fun fromWeaponType(type: EquipmentType): CharacterClass? = TYPE_MAP[type]

        fun fromDisplayName(displayName: String): CharacterClass? = NAME_MAP[displayName]
    }

    fun getMainAttackKey(): MouseKey = mainKey

    fun getSpellKey(): MouseKey = mainKey.opposite()

    fun getWeapon(): EquipmentType = EquipmentType.getEquipmentType(weaponName)

    override fun getKey(): String = name

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.class.${getKey().lowercase(Locale.getDefault())}"
    }
}