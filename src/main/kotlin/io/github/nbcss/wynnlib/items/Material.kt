package io.github.nbcss.wynnlib.items

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.CraftedType
import io.github.nbcss.wynnlib.data.Profession
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_CRAFTING_MAT
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_GATHERING_LV_REQ
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_MATERIAL_RECIPES
import io.github.nbcss.wynnlib.items.identity.ConfigurableItem
import io.github.nbcss.wynnlib.matcher.MatchableItem
import io.github.nbcss.wynnlib.matcher.MatcherType
import io.github.nbcss.wynnlib.registry.RecipeRegistry
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.ItemFactory.ERROR_ITEM
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import kotlin.math.max
import kotlin.math.min

class Material(json: JsonObject) : Keyed, BaseItem, MatchableItem, ConfigurableItem {
    private val id: String = json["id"].asString
    private val name: String = json["name"].asString
    private val displayName: String = json["displayName"].asString
    private val tier: Tier = Tier.fromStar(json["tier"].asInt)
    private val type: Type = Type.fromName(json["type"].asString) ?: Type.INGOT
    private val level: Int = json["level"].asInt
    private val texture: ItemStack = if (json.has("skin")){
        ItemFactory.fromSkin(json["skin"].asString)
    }else if(json.has("texture")){
        ItemFactory.fromEncoding(json["texture"].asString)
    }else{
        ERROR_ITEM
    }

    fun getName(): String = name

    fun getType(): Type = type

    fun getTier(): Tier = tier

    fun getItemName(): String = "${Formatting.WHITE}" + displayName + tier.suffix

    override fun getDisplayText(): Text {
        return LiteralText(displayName).formatted(Formatting.WHITE).append(tier.suffix)
    }

    override fun getDisplayName(): String = displayName

    override fun getRarityColor(): Color {
        return getMatcherType().getColor()
    }

    override fun getIconText(): String = getType().getProfession().getIconSymbol()

    override fun getIcon(): ItemStack = texture

    override fun getKey(): String = id

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
        val recipes: MutableMap<CraftedType, IRange> = linkedMapOf()
        RecipeRegistry.fromMaterial(this).forEach {
            val level = recipes[it.getType()] ?: it.getLevel()
            val lower = min(level.lower(), it.getLevel().lower())
            val upper = max(level.upper(), it.getLevel().upper())
            recipes[it.getType()] = SimpleIRange(lower, upper)
        }
        recipes.forEach { (type, level) ->
            tooltip.add(LiteralText(" - ").formatted(Formatting.DARK_GRAY)
                .append(type.getProfession().getIconText()).append(" ")
                .append(type.formatted(Formatting.WHITE))
                .append(LiteralText(" [${level.lower()}-${level.upper()}]").formatted(Formatting.DARK_GRAY)))
        }
        return tooltip
    }

    override fun getMatcherType(): MatcherType {
        return MatcherType.fromMaterialTier(tier)
    }

    override fun asBaseItem(): BaseItem {
        return this
    }

    override fun getConfigDomain(): String {
        return "MATERIAL"
    }

    enum class Tier(val suffix: String, val color: Color, val coefficient: Double) {
        STAR_1("§6 [§e✫§8✫✫§6]", Color.YELLOW, 1.0),
        STAR_2("§6 [§e✫✫§8✫§6]", Color.PINK, 1.25),
        STAR_3("§6 [§e✫✫✫§6]", Color.AQUA, 1.4);

        fun getStars(): Int = ordinal + 1

        companion object {
            fun fromStar(star: Int): Tier {
                return when(star){
                    3 -> STAR_3
                    2 -> STAR_2
                    else -> STAR_1
                }
            }
        }
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
        companion object {
            private val VALUE_MAP: Map<String, Type> = mapOf(
                pairs = values().map { it.name to it }.toTypedArray()
            )

            fun fromName(name: String): Type? {
                return VALUE_MAP[name.uppercase()]
            }
        }

        fun getProfession(): Profession = profession
    }
}