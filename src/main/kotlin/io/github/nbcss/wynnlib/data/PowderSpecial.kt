package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable

enum class PowderSpecial: Translatable {
    QUAKE,
    CHAIN_LIGHTING,
    CURSE,
    COURAGE,
    WIND_PRISON,
    RAGE,
    KILL_STREAK,
    CONCENTRATION,
    ENDURANCE,
    DODGE;
    companion object {
        fun fromWeaponElement(element: Element): PowderSpecial {
            return when (element){
                Element.FIRE -> COURAGE
                Element.WATER -> CURSE
                Element.AIR -> WIND_PRISON
                Element.THUNDER -> CHAIN_LIGHTING
                Element.EARTH -> QUAKE
            }
        }

        fun fromArmourElement(element: Element): PowderSpecial {
            return when (element){
                Element.FIRE -> ENDURANCE
                Element.WATER -> CONCENTRATION
                Element.AIR -> DODGE
                Element.THUNDER -> KILL_STREAK
                Element.EARTH -> RAGE
            }
        }
    }

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.powder_spec.${name.lowercase()}"
    }
}