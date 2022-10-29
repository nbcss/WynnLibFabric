package io.github.nbcss.wynnlib.items

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.ItemFactory
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

class Emerald(private val tier: Tier) : BaseItem, Translatable {

    override fun getDisplayText(): Text {
        return formatted(Formatting.GREEN, tier.name.lowercase())
    }

    override fun getDisplayName(): String {
        return translate(getTranslationKey(tier.name.lowercase())).string
    }

    override fun getTooltip(): List<Text> {
        return listOf(
            getDisplayText(),
            formatted(Formatting.GRAY, "desc"),
            LiteralText.EMPTY,
            LiteralText("----[ ").formatted(Formatting.DARK_GREEN).append(
                LiteralText("${tier.value}²").formatted(Formatting.GREEN)
            ).append(
                LiteralText(" ]----").formatted(Formatting.DARK_GREEN)
            )
        )
    }

    fun getTier(): Tier = tier

    override fun getIcon(): ItemStack {
        return tier.icon
    }

    override fun getRarityColor(): Color {
        return tier.color
    }

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.emerald.$label"
    }

    enum class Tier(val value: Int, val color: Color, val icon: ItemStack) {
        // color from light green to deep green
        BASIC(1, Color(0xD3F1C3), ItemFactory.fromEncoding("minecraft:emerald")),
        BLOCK(64, Color(0x67D755), ItemFactory.fromEncoding("minecraft:emerald_block")),
        LIQUID(4096, Color(0x36AB3A), ItemFactory.fromEncoding("minecraft:experience_bottle")),
    }


}