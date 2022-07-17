package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translations
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

interface PowderSpecial {
    fun getType(): Type
    fun getTooltip(): List<Text>

    class Quake(private val radius: Double,
                private val damage: Int) : PowderSpecial {

        override fun getType(): Type = Type.QUAKE

        override fun getTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            val color = getType().getElement().color
            tooltip.add(getType().formatted(color))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("Radius: ").formatted(Formatting.GRAY))
                .append(Translations.TOOLTIP_SUFFIX_BLOCKS.formatted(Formatting.GRAY, label = null, radius)))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("Damage: ${damage}% ✤").formatted(Formatting.GRAY)))
            return tooltip
        }
    }

    class Rage(private val damage: Double) : PowderSpecial {

        override fun getType(): Type = Type.RAGE

        override fun getTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            val color = getType().getElement().color
            tooltip.add(getType().formatted(color))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("Damage: +${damage}% ✤").formatted(Formatting.GRAY)))
            return tooltip
        }
    }

    class ChainLighting(private val chains: Int,
                        private val damage: Int) : PowderSpecial {

        override fun getType(): Type = Type.CHAIN_LIGHTING

        override fun getTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            val color = getType().getElement().color
            tooltip.add(getType().formatted(color))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("Chains: $chains").formatted(Formatting.GRAY)))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("Damage: ${damage}% ✦").formatted(Formatting.GRAY)))
            return tooltip
        }
    }

    enum class Type(private val element: Element): Translatable {
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
            fun fromWeaponElement(element: Element): Type {
                return when (element){
                    Element.FIRE -> COURAGE
                    Element.WATER -> CURSE
                    Element.AIR -> WIND_PRISON
                    Element.THUNDER -> CHAIN_LIGHTING
                    Element.EARTH -> QUAKE
                }
            }

            fun fromArmourElement(element: Element): Type {
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
}