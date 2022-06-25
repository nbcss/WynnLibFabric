package io.github.nbcss.wynnlib.utils

import net.minecraft.util.Formatting

class Color(val red: Int,
            val green: Int,
            val blue: Int) {
    constructor(code: Int): this(code shr 16 and 0xFF, code shr 8 and 0xFF, code and 0xFF)

    companion object {
        val BLACK = Color(0x000000)
        val DARK_BLUE = Color(0x0000AA)
        val DARK_GREEN = Color(0x00AA00)
        val DARK_AQUA = Color(0x00AAAA)
        val DARK_RED = Color(0xAA0000)
        val DARK_PURPLE = Color(0xAA00AA)
        val ORANGE = Color(0xFFAA00)
        val GRAY = Color(0xAAAAAA)
        val DARK_GRAY = Color(0x555555)
        val BLUE = Color(0x5555FF)
        val GREEN = Color(0x55FF55)
        val AQUA = Color(0x55FFFF)
        val RED = Color(0xFF5555)
        val PINK = Color(0xFF55FF)
        val YELLOW = Color(0xFFFF55)
        val WHITE = Color(0xFFFFFF)

        fun fromFormatting(formatting: Formatting): Color {
            return when(formatting){
                Formatting.BLACK -> BLACK
                Formatting.DARK_BLUE -> DARK_BLUE
                Formatting.DARK_GREEN -> DARK_GREEN
                Formatting.DARK_AQUA -> DARK_AQUA
                Formatting.DARK_RED -> DARK_RED
                Formatting.DARK_PURPLE -> DARK_PURPLE
                Formatting.GOLD -> ORANGE
                Formatting.GRAY -> GRAY
                Formatting.DARK_GRAY -> DARK_GRAY
                Formatting.BLUE -> BLUE
                Formatting.GREEN -> GREEN
                Formatting.AQUA -> AQUA
                Formatting.RED -> RED
                Formatting.LIGHT_PURPLE -> PINK
                Formatting.YELLOW -> YELLOW
                else -> WHITE
            }
        }

        fun normalize(value: Int): Float = value / 255.0f
    }
    /**
     * Get the color code of the color (without alpha).
     *
     * @return color code
     */
    fun getColorCode(): Int = (red shl 16) + (green shl 8) + blue

    /**
     * Get the color code of the color with given alpha.
     * Note that the method will NOT check if given alpha is in range!
     *
     * @param alpha alpha value, should be in [0, 255]. (8 bits)
     * @return color code with the given alpha value.
     */
    fun getColorCode(alpha: Int): Int {
        return getColorCode() + (alpha shl 24)
    }

    fun floatRed(): Float = normalize(red)

    fun floatGreen(): Float = normalize(green)

    fun floatBlue(): Float = normalize(blue)
}