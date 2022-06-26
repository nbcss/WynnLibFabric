package io.github.nbcss.wynnlib.items

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.Element
import io.github.nbcss.wynnlib.data.PowderSpecial
import io.github.nbcss.wynnlib.data.Skill
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_ING_DURABILITY
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_ARMOUR
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_CONVERT
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_CRAFTING
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPECIAL
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_WEAPON
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_SKILL_MODIFIER
import io.github.nbcss.wynnlib.utils.*
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper
import java.util.*
import kotlin.collections.ArrayList

class Powder(json: JsonObject) : Keyed, BaseItem {
    private val skillMap: MutableMap<Skill, Int> = EnumMap(Skill::class.java)
    private val name: String
    private val displayName: String
    private val tier: Tier
    private val minDamageBonus: Int
    private val maxDamageBonus: Int
    private val convertRate: Int
    private val defenceBonus: Int
    private val defencePenalty: Int
    private val durability: Double
    private val element: Element
    private val oppoElem: Element
    private val texture: ItemStack
    init {
        name = json["name"].asString
        displayName = json["displayName"].asString
        tier = Tier.values()[MathHelper.clamp(json["tier"].asInt - 1, 0, Tier.values().size - 1)]
        minDamageBonus = json["minDamageBonus"].asInt
        maxDamageBonus = json["maxDamageBonus"].asInt
        convertRate = json["damageConvert"].asInt
        defenceBonus = json["defenceBonus"].asInt
        defencePenalty = json["oppositeDefenceBonus"].asInt
        element = Element.fromId(json["element"].asString)!!
        oppoElem = Element.fromId(json["oppositeElement"].asString)!!
        texture = ItemFactory.fromEncoding(json["texture"].asString)
        val itemOnly = json.get("itemOnlyIDs").asJsonObject
        durability = itemOnly["durabilityModifier"].asDouble
        Skill.values().forEach {
            val value = if (itemOnly.has(it.requirementName)) itemOnly[it.requirementName].asInt else 0
            if (value != 0){
                skillMap[it] = value
            }
        }
    }

    fun getTier(): Tier = tier

    fun getElement(): Element = element

    override fun getKey(): String = name

    override fun getDisplayText(): Text = LiteralText(displayName).formatted(element.color)

    override fun getDisplayName(): String = displayName

    override fun getIcon(): ItemStack = texture

    override fun getRarityColor(): Color {
        return Settings.getColor("powder_tier", tier.name)
    }

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(getDisplayText())
        tooltip.add(LiteralText.EMPTY)
        tooltip.add(TOOLTIP_POWDER_WEAPON.translate().formatted(element.altColor)
            .append(LiteralText(":").formatted(element.altColor)))
        val prefix = LiteralText("- ")
        tooltip.add(prefix.copy().formatted(element.altColor)
            .append(LiteralText("+${minDamageBonus}-${maxDamageBonus} ").formatted(Formatting.GRAY))
            .append(element.formatted(Formatting.GRAY, "tooltip.damage")))
        tooltip.add(prefix.copy().formatted(element.altColor)
            .append(LiteralText("+${convertRate}% ").formatted(Formatting.GRAY))
            .append(TOOLTIP_POWDER_CONVERT.formatted(Formatting.GRAY))
            .append(element.formatted(Formatting.GRAY, "tooltip.damage")))
        if (tier.index() >= 4){
            val spec = PowderSpecial.fromWeaponElement(element)
            tooltip.add(prefix.copy().formatted(element.altColor)
                .append(LiteralText("+").formatted(Formatting.GRAY))
                .append(TOOLTIP_POWDER_SPECIAL.formatted(Formatting.GRAY))
                .append(LiteralText(": ").formatted(Formatting.GRAY))
                .append(spec.formatted(element.color)))
        }
        tooltip.add(LiteralText.EMPTY)
        tooltip.add(TOOLTIP_POWDER_ARMOUR.translate().formatted(element.altColor)
            .append(LiteralText(":").formatted(element.altColor)))
        tooltip.add(prefix.copy().formatted(element.altColor)
            .append(LiteralText("+${defenceBonus} ").formatted(Formatting.GRAY))
            .append(element.formatted(Formatting.GRAY, "tooltip.defence")))
        tooltip.add(prefix.copy().formatted(element.altColor)
            .append(LiteralText("$defencePenalty ").formatted(Formatting.GRAY))
            .append(oppoElem.formatted(Formatting.GRAY, "tooltip.defence")))
        if (tier.index() >= 4){
            val spec = PowderSpecial.fromArmourElement(element)
            tooltip.add(prefix.copy().formatted(element.altColor)
                .append(LiteralText("+").formatted(Formatting.GRAY))
                .append(TOOLTIP_POWDER_SPECIAL.formatted(Formatting.GRAY))
                .append(LiteralText(": ").formatted(Formatting.GRAY))
                .append(spec.formatted(element.color)))
        }
        if(durability != 0.0 || skillMap.isNotEmpty()){
            tooltip.add(LiteralText.EMPTY)
            tooltip.add(TOOLTIP_POWDER_CRAFTING.translate().formatted(element.altColor)
                .append(LiteralText(":").formatted(element.altColor)))
            if (durability != 0.0){
                val value = if (durability.toInt().toDouble() == durability)
                    durability.toInt().toString() else durability.toString()
                val color = colorOf(durability.toInt())
                tooltip.add(LiteralText(value).formatted(color).append(" ")
                    .append(TOOLTIP_ING_DURABILITY.translate().formatted(color)))
            }
            Skill.values().forEach { skill ->
                skillMap[skill]?.let {
                    if (it != 0){
                        val color = colorOf(-it)
                        val modifier = TOOLTIP_SKILL_MODIFIER.translate(null, skill.translate().string)
                        tooltip.add(LiteralText("${signed(it)} ").formatted(color)
                            .append(modifier.formatted(color)))
                    }
                }
            }
        }
        return tooltip
    }

    override fun getIconText(): String {
        return tier.name
    }

    enum class Tier {
        I,
        II,
        III,
        IV,
        V,
        VI;
        fun index(): Int = ordinal + 1
    }
}