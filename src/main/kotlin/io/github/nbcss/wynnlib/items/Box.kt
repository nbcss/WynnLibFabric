package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.analysis.TransformableItem
import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.items.equipments.Equipment
import io.github.nbcss.wynnlib.registry.CharmRegistry
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry
import io.github.nbcss.wynnlib.registry.TomeRegistry
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.range.IRange
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class Box(type: EquipmentType, tier: Tier, levelRange: IRange) : BaseItem, Translatable, TransformableItem {
    private val type: EquipmentType = type
    private val tier: Tier = tier
    private val minLevel: Int = levelRange.lower()
    private val maxLevel: Int = levelRange.upper()
    private val texture: ItemStack
    private val potentialItems: List<Equipment>

    init {
        texture = ItemFactory.fromEncoding(
            "minecraft:stone_shovel#${
                listOf<Tier>(
                    Tier.UNIQUE,
                    Tier.RARE,
                    Tier.SET,
                    Tier.LEGENDARY,
                    Tier.FABLED,
                    Tier.MYTHIC // The order is same as the texture
                ).indexOf(tier)
            }"
        )
        potentialItems = if (type == EquipmentType.TOME) {
            TomeRegistry.getAll().filter {
                it.getTier() == tier &&
                        it.getLevel().lower() > minLevel &&
                        it.getLevel().upper() <= maxLevel &&
                        it.getType() == type // Expect it to be constant true
            }
        } else if (type == EquipmentType.CHARM) {
            CharmRegistry.getAll().filter {
                it.getTier() == tier &&
                        it.getLevel().lower() > minLevel &&
                        it.getLevel().upper() <= maxLevel &&
                        it.getType() == type // Expect it to be constant true

            }
        } else {
            RegularEquipmentRegistry.getAll().filter {
                it.getTier() == tier &&
                        it.getLevel().lower() > minLevel &&
                        it.getLevel().upper() <= maxLevel &&
                        it.getType() == type
            }
        }
    }

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.box"
    }

    override fun getDisplayText(): Text {
        return formatted(tier.formatting)
    }

    override fun getDisplayName(): String {
        return translate("unidentified").append(" ").append(type.translate()).string
    }

    override fun getIcon(): ItemStack = texture

    override fun getRarityColor(): Color = Settings.getTierColor(tier)

    override fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        tooltip.addAll(super.getTooltip())
        for (i in 1..4) {
            if (translate("desc.line$i").string != "") {
                tooltip.add(LiteralText(translate("desc.line$i").string).formatted(Formatting.GRAY))
            }
        }
        tooltip.add(LiteralText.EMPTY)
        tooltip.add(LiteralText(translate("info").string).formatted(Formatting.GREEN))
        tooltip.add(
            LiteralText("- ").formatted(Formatting.GREEN)
                .append(LiteralText(translate("lvRange").string).formatted(Formatting.GRAY))
                .append(LiteralText(" $minLevel - $maxLevel").formatted(Formatting.WHITE))
        )
        tooltip.add(
            LiteralText("- ").formatted(Formatting.GREEN)
                .append(LiteralText(translate("tier").string).formatted(Formatting.GRAY))
                .append(LiteralText(" ${tier.translate()}").formatted(tier.formatting))
        )
        tooltip.add(
            LiteralText("- ").formatted(Formatting.GREEN)
                .append(LiteralText(translate("type").string).formatted(Formatting.GRAY))
                .append(LiteralText(" ${type.translate()}").formatted(Formatting.WHITE))
        )
        if (potentialItems.isEmpty()) {
            tooltip.add(
                LiteralText(translate("analysis_failed").string).formatted(Formatting.RED)
            ) // This shouldn't happen unless new items are added
        }
        tooltip.add(
            LiteralText("- ").formatted(Formatting.GREEN)
                .append(
                    if (potentialItems.size == 1) {
                        LiteralText(translate("potential_item").string).formatted(Formatting.GRAY)
                    } else {
                        LiteralText(translate("potential_items").string).formatted(Formatting.GRAY)
                    }
                )
        )
        if (potentialItems.size == 1) {
            tooltip.addAll(potentialItems[0].getTooltip())
        } else {
            for (item in potentialItems) {
                tooltip.add(
                    LiteralText("- ").formatted(Formatting.GRAY)
                        .append(
                            LiteralText("[${item.getTier().getIdentifyPrice(item.getLevel().lower())}]").formatted(
                                Formatting.GREEN
                            )
                        )
                        .append(item.getDisplayText())  // TODO Add Underline for starred items
                        .append(LiteralText(" (${item.getLevel().lower()})").formatted(Formatting.GOLD))
                )
            }
        }
        return tooltip
    }

    override fun getIconText(): String? {
        return super.getIconText()
    }

    override fun getTransformKey(): String {
        TODO("Not yet implemented")
    }
}