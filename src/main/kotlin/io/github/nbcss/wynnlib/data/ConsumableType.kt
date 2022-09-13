package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import net.minecraft.text.Text
import net.minecraft.util.Formatting

enum class ConsumableType(private val prefix: Formatting): Translatable {
    HEALING_POTION(Formatting.RED),
    MANA_POTION(Formatting.AQUA),
    WISDOM_POTION(Formatting.GOLD),
    STRENGTH_POTION(Formatting.GREEN),
    DEXTERITY_POTION(Formatting.GREEN),
    INTELLIGENCE_POTION(Formatting.GREEN),
    DEFENCE_POTION(Formatting.GREEN),
    AGILITY_POTION(Formatting.GREEN),
    CRAFTED_POTION(Formatting.DARK_AQUA),
    CRAFTED_FOOD(Formatting.DARK_AQUA),
    CRAFTED_SCROLL(Formatting.DARK_AQUA);

    fun getDisplayText(): Text {
        return formatted(prefix)
    }

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.consumable_type.${name.lowercase()}"
    }
}