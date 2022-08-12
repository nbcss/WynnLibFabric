package io.github.nbcss.wynnlib.utils

import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.util.Formatting

enum class Symbol(val icon: String,
                  val formatting: Formatting) {
    MANA("✺", Formatting.AQUA),
    DARK_MANA("✺", Formatting.DARK_AQUA),
    HEART("❤", Formatting.RED),
    DARK_HEART("❤", Formatting.DARK_RED),
    MAX("▲", Formatting.GRAY),
    TICK("✔", Formatting.GREEN),
    CROSS("✖", Formatting.RED),
    RANGE("➼", Formatting.DARK_GREEN),
    AOE("☀", Formatting.DARK_AQUA),
    DURATION("⌛", Formatting.LIGHT_PURPLE),
    EFFECT("\uD83D\uDEE1", Formatting.YELLOW),
    CHANCE("％", Formatting.DARK_GREEN),
    DAMAGE("⚔", Formatting.RED),
    COOLDOWN("⌛", Formatting.AQUA),
    DAMAGE_INTERVAL("⌛", Formatting.RED),
    CHARGE("⚡", Formatting.GREEN),
    HITS("☄", Formatting.YELLOW),
    ALTER_HITS("☄", Formatting.GREEN),
    REPLACE("✚", Formatting.LIGHT_PURPLE),
    WARNING("⚠", Formatting.DARK_RED);

    fun asText(): MutableText {
        return LiteralText(icon).formatted(formatting)
    }
}