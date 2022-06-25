package io.github.nbcss.wynnlib.items

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.Profession
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_CRAFTING_MAT
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_GATHERING_LV_REQ
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_MATERIAL_RECIPES
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.ERROR_ITEM
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class Material(private val tier: Tier, json: JsonObject) : Keyed, BaseItem {
    private val name: String
    private val displayName: String
    private val type: Type
    private val level: Int
    private val texture: ItemStack
    init {
        name = "${json["name"].asString} ${tier.getStars()}"
        displayName = json["displayName"].asString
        type = Type.valueOf(json["type"].asString.uppercase())
        level = json["level"].asInt
        texture = if (json.has("skin")){
            ItemFactory.fromSkin(json["skin"].asString)
        }else if(json.has("texture")){
            ItemFactory.fromEncoding(json["texture"].asString)
        }else{
            ERROR_ITEM
        }
    }

    fun getType(): Type = type

    override fun getDisplayText(): Text {
        return LiteralText(displayName).formatted(Formatting.WHITE).append(tier.suffix)
    }

    override fun getDisplayName(): String = displayName

    override fun getRarityColor(): Color {
        return Settings.getColor("material_tier", tier.name)
    }

    override fun getIconText(): String = getType().getProfession().getIconSymbol()

    override fun getIcon(): ItemStack = texture

    override fun getKey(): String = name

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(getDisplayText())
        tooltip.add(TOOLTIP_CRAFTING_MAT.translate().formatted(Formatting.GRAY))
        tooltip.add(LiteralText.EMPTY)
        val profession = getType().getProfession().translate().string
        tooltip.add(LiteralText("${getType().getProfession().getIconSymbol()} ").formatted(Formatting.WHITE)
            .append(TOOLTIP_GATHERING_LV_REQ.translate(null, profession).formatted(Formatting.GRAY))
            .append(LiteralText(": ").formatted(Formatting.GRAY))
            .append(LiteralText("$level").formatted(Formatting.WHITE)))
        tooltip.add(LiteralText.EMPTY)
        tooltip.add(TOOLTIP_MATERIAL_RECIPES.translate().formatted(Formatting.GRAY))
        val prefix = LiteralText(" - ")
        //todo add recipe list
        tooltip.add(prefix.copy().formatted(Formatting.DARK_GRAY)
            .append(LiteralText("???").formatted(Formatting.WHITE)))
        return tooltip
    }

    enum class Tier(val suffix: String, val coefficient: Double) {
        STAR_1("§6 [§e✫§8✫✫§6]", 1.0),
        STAR_2("§6 [§e✫✫§8✫§6]", 1.25),
        STAR_3("§6 [§e✫✫✫§6]", 1.4);

        fun getStars(): Int = ordinal + 1
    }

    enum class Type(private val profession: Profession) {
        INGOT(Profession.MINING),
        GEM(Profession.MINING),
        WOOD(Profession.WOODCUTTING),
        PAPER(Profession.WOODCUTTING),
        STRING(Profession.FARMING),
        GRAINS(Profession.FARMING),
        OIL(Profession.FISHING),
        MEAT(Profession.FISHING);

        fun getProfession(): Profession = profession
    }
}