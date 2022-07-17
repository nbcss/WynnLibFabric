package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable

enum class PowderSpecial(private val element: Element): Translatable {
    QUAKE(Element.EARTH),
    CHAIN_LIGHTING(Element.THUNDER),
    CURSE(Element.WATER),
    COURAGE(Element.FIRE),
    WIND_PRISON(Element.AIR),
    RAGE(Element.EARTH),
    KILL_STREAK(Element.THUNDER),
    CONCENTRATION(Element.WATER),
    ENDURANCE(Element.FIRE),
    DODGE(Element.AIR);
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

    fun getElement(): Element = element

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.powder_spec.${name.lowercase()}"
    }
}