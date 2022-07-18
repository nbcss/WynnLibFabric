package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.SUFFIX_POWDER_SPEC_BLOCKS
import io.github.nbcss.wynnlib.i18n.Translations.SUFFIX_POWDER_SPEC_DAM_PER_MANA
import io.github.nbcss.wynnlib.i18n.Translations.SUFFIX_POWDER_SPEC_SEC
import io.github.nbcss.wynnlib.i18n.Translations.SUFFIX_POWDER_SPEC_SEC_PER_MANA
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_CHAINS
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_DAMAGE
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_DAMAGE_BOOST
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_DURATION
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_KNOCKBACK
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_RADIUS
import io.github.nbcss.wynnlib.utils.removeDecimal
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

interface PowderSpecial {
    companion object {

    }
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
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_RADIUS.translate().string}: ")
                    .formatted(Formatting.GRAY))
                .append(SUFFIX_POWDER_SPEC_BLOCKS.formatted(Formatting.GRAY, label = null, radius)))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: ${damage}% ✤")
                    .formatted(Formatting.GRAY)))
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
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: +${damage}% ✤")
                    .formatted(Formatting.GRAY)))
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
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_CHAINS.translate().string}: $chains")
                    .formatted(Formatting.GRAY)))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: ${damage}% ✦")
                    .formatted(Formatting.GRAY)))
            return tooltip
        }
    }

    class KillStreak(private val damage: Double,
                     private val duration: Double) : PowderSpecial {

        override fun getType(): Type = Type.KILL_STREAK

        override fun getTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            val color = getType().getElement().color
            tooltip.add(getType().formatted(color))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: ${damage}% ✦")
                    .formatted(Formatting.GRAY)))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY))
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
            return tooltip
        }
    }

    class Courage(private val duration: Double,
                  private val damage: Double,
                  private val boost: Double) : PowderSpecial {

        override fun getType(): Type = Type.COURAGE

        override fun getTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            val color = getType().getElement().color
            tooltip.add(getType().formatted(color))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY))
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: ${damage}% ✹")
                    .formatted(Formatting.GRAY)))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE_BOOST.translate().string}: " +
                        "+${removeDecimal(boost)}%")
                    .formatted(Formatting.GRAY)))
            return tooltip
        }
    }

    class Endurance(private val damage: Double,
                    private val duration: Double) : PowderSpecial {

        override fun getType(): Type = Type.ENDURANCE

        override fun getTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            val color = getType().getElement().color
            tooltip.add(getType().formatted(color))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: " +
                        "+${removeDecimal(damage)}% ✹")
                    .formatted(Formatting.GRAY)))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY))
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
            return tooltip
        }
    }

    class Curse(private val duration: Double,
                private val boost: Double) : PowderSpecial {

        override fun getType(): Type = Type.CURSE

        override fun getTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            val color = getType().getElement().color
            tooltip.add(getType().formatted(color))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY))
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE_BOOST.translate().string}: " +
                        "+${removeDecimal(boost)}%")
                    .formatted(Formatting.GRAY)))
            return tooltip
        }
    }

    class Concentration(private val damage: Double,
                        private val duration: Double) : PowderSpecial {

        override fun getType(): Type = Type.CONCENTRATION

        override fun getTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            val color = getType().getElement().color
            tooltip.add(getType().formatted(color))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: ")
                    .formatted(Formatting.GRAY))
                .append(SUFFIX_POWDER_SPEC_DAM_PER_MANA.formatted(Formatting.GRAY, label = null,
                    "+${removeDecimal(damage)}%")))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY))
                .append(SUFFIX_POWDER_SPEC_SEC_PER_MANA.formatted(Formatting.GRAY, label = null,
                    removeDecimal(duration))))
            return tooltip
        }
    }

    class WindPrison(private val duration: Double,
                     private val boost: Double,
                     private val knockback: Int) : PowderSpecial {

        override fun getType(): Type = Type.WIND_PRISON

        override fun getTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            val color = getType().getElement().color
            tooltip.add(getType().formatted(color))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY))
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE_BOOST.translate().string}: " +
                        "+${removeDecimal(boost)}% ❋")
                    .formatted(Formatting.GRAY)))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_KNOCKBACK.translate().string}: ")
                    .formatted(Formatting.GRAY))
                .append(SUFFIX_POWDER_SPEC_BLOCKS.formatted(Formatting.GRAY, label = null, signed(knockback))))
            return tooltip
        }
    }

    class Dodge(private val damage: Double,
                private val duration: Double) : PowderSpecial {

        override fun getType(): Type = Type.DODGE

        override fun getTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            val color = getType().getElement().color
            tooltip.add(getType().formatted(color))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: " +
                        "+${removeDecimal(damage)}% ❋")
                    .formatted(Formatting.GRAY)))
            tooltip.add(LiteralText("- ").formatted(color)
                .append(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY))
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
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