package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.analysis.TransformableItem
import io.github.nbcss.wynnlib.analysis.transformers.UnidentifiedBoxTransformer
import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.i18n.Translatable.Companion.from
import io.github.nbcss.wynnlib.items.equipments.Equipment
import io.github.nbcss.wynnlib.items.identity.ConfigurableItem
import io.github.nbcss.wynnlib.items.identity.ItemStarProperty
import io.github.nbcss.wynnlib.items.identity.ProtectableItem
import io.github.nbcss.wynnlib.matcher.MatchableItem
import io.github.nbcss.wynnlib.matcher.MatcherType
import io.github.nbcss.wynnlib.registry.CharmRegistry
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry
import io.github.nbcss.wynnlib.registry.TomeRegistry
import io.github.nbcss.wynnlib.utils.*
import io.github.nbcss.wynnlib.utils.range.IRange
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class UnidentifiedBox(private val type: EquipmentType,
                      private val tier: Tier,
                      levelRange: IRange):
    BaseItem, TransformableItem, MatchableItem, ProtectableItem {
    companion object {
        private val NAME = from("wynnlib.unidentified_box.name")
        private val DESCRIPTION = from("wynnlib.unidentified_box.desc")
        private val INFO = from("wynnlib.unidentified_box.info")
        private val LV_RANGE = from("wynnlib.unidentified_box.lv_range")
        private val TIER = from("wynnlib.unidentified_box.tier")
        private val TYPE = from("wynnlib.unidentified_box.type")
        private val ITEM_LIST = from("wynnlib.unidentified_box.potential_item")
        private val ITEM_LIST_MULTI = from("wynnlib.unidentified_box.potential_items")
    }
    private val minLevel: Int = levelRange.lower()
    private val maxLevel: Int = levelRange.upper()
    private val texture: ItemStack = ItemFactory.fromEncoding(
        "minecraft:stone_shovel#${
            listOf(
                Tier.UNIQUE,
                Tier.RARE,
                Tier.SET,
                Tier.LEGENDARY,
                Tier.FABLED,
                Tier.MYTHIC // The order is same as the texture
            ).indexOf(tier)
        }"
    )
    private val potentialItems: List<Equipment> = when (type) {
        EquipmentType.TOME -> {
            TomeRegistry.getAll()
                .filter { it.getTier() == tier}
                .filter { it.getLevel().lower() > minLevel }
                .filter { it.getLevel().upper() <= maxLevel }
                .sortedBy { it.getLevel().lower() }
        }
        EquipmentType.CHARM -> {
            CharmRegistry.getAll()
                .filter { it.getTier() == tier}
                .filter { it.getLevel().lower() > minLevel }
                .filter { it.getLevel().upper() <= maxLevel }
                .sortedBy { it.getLevel().lower() }
        }
        else -> {
            RegularEquipmentRegistry.getAll()
                .asSequence()
                .filter { it.getType() == type }
                .filter { it.getTier() == tier}
                .filter { it.isIdentifiable() }
                .filter { it.getLevel().lower() > minLevel }
                .filter { it.getLevel().upper() <= maxLevel }
                .sortedBy { it.getLevel().lower() }
                .toList()
        }
    }

    override fun getTransformKey(): String {
        return UnidentifiedBoxTransformer.KEY
    }

    override fun getDisplayText(): Text {
        return LiteralText(getDisplayName()).formatted(tier.formatting)
    }

    override fun getDisplayName(): String {
        return NAME.translate(null, type.translate().string).string
    }

    override fun getIcon(): ItemStack = texture

    override fun getRarityColor(): Color = MatcherType.fromItemTier(tier).getColor()

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        tooltip.add(getDisplayText())
        tooltip.addAll(formattingLines(DESCRIPTION.translate().string, "${Formatting.GRAY}", 200))
        tooltip.add(LiteralText.EMPTY)
        tooltip.add(INFO.formatted(Formatting.GREEN).append(":"))
        tooltip.add(LiteralText("- ").formatted(Formatting.GREEN)
            .append(LV_RANGE.formatted(Formatting.GRAY).append(": "))
            .append(LiteralText("$minLevel-$maxLevel").formatted(Formatting.WHITE))
        )
        tooltip.add(LiteralText("- ").formatted(Formatting.GREEN)
            .append(TIER.formatted(Formatting.GRAY).append(": "))
            .append(tier.getDisplayText())
        )
        tooltip.add(LiteralText("- ").formatted(Formatting.GREEN)
            .append(TYPE.formatted(Formatting.GRAY).append(": "))
            .append(type.formatted(Formatting.WHITE))
        )
        if (potentialItems.isNotEmpty()) {
            val title = if (potentialItems.size == 1) ITEM_LIST else ITEM_LIST_MULTI
            tooltip.add(LiteralText("- ").formatted(Formatting.GREEN)
                .append(title.formatted(Formatting.GRAY)))
            if (potentialItems.size == 1) {
                tooltip.addAll(potentialItems[0].getTooltip())
            } else {
                for (item in potentialItems) {
                    val price = formatNumbers(tier.getIdentifyPrice(item.getLevel().lower()))
                    val name = LiteralText(item.getDisplayName()).formatted(item.getTier().formatting)
                    if (item is ConfigurableItem && ItemStarProperty.hasStar(item)) {
                        name.formatted(Formatting.UNDERLINE)
                    }
                    tooltip.add(LiteralText("- ").formatted(Formatting.GRAY)
                        .append(LiteralText("[$price²] ").formatted(Formatting.GREEN))
                        .append(name)
                        .append(LiteralText(" (${item.getLevel().lower()})").formatted(Formatting.GOLD)))
                }
            }
        }
        return tooltip
    }

    override fun getMatcherType(): MatcherType {
        return MatcherType.fromItemTier(tier)
    }

    override fun asBaseItem(): BaseItem {
        return this
    }

    override fun isProtected(): Boolean {
        if (!Settings.getOption(Settings.SettingOption.STARRED_ITEM_PROTECT))
            return false
        return potentialItems
            .filter { it is ConfigurableItem }
            .map { it as ConfigurableItem }
            .any { ItemStarProperty.hasStar(it) }
    }
}