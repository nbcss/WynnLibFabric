package io.github.nbcss.wynnlib.utils

import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.util.Formatting

enum class Symbol(private val icon: String,
                  private val formatting: Formatting) {
    MANA("✺", Formatting.AQUA),
    RANGE("➼", Formatting.DARK_GREEN),
    AOE("☀", Formatting.DARK_AQUA),
    DURATION("⌛", Formatting.LIGHT_PURPLE),
    EFFECT("\uD83D\uDEE1", Formatting.YELLOW),
    DAMAGE("⚔", Formatting.RED),
    COOLDOWN("⌛", Formatting.GOLD),
    CHARGE("⚡", Formatting.GREEN),
    HITS("☄", Formatting.YELLOW),
    ALTER_HITS("☄", Formatting.GREEN),
    ADD("✚", Formatting.LIGHT_PURPLE),
    WARNING("⚠", Formatting.DARK_RED),
    ;
    //CHARGE ⚡

    fun asText(): MutableText {
        return LiteralText(icon).formatted(formatting)
    }
}