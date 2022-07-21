package io.github.nbcss.wynnlib.utils

import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.text.*
import net.minecraft.util.Formatting
import java.util.*
import java.util.function.Function
import kotlin.math.roundToInt

fun signed(value: Int): String {
    return if(value <= 0) value.toString() else "+$value"
}

fun signed(value: Double): String {
    return if(value <= 0.0) value.toString() else "+$value"
}

fun round(value: Double): Double {
    return (value * 1000).roundToInt() / 1000.0
}

fun removeDecimal(value: Double): String {
    return (if (value % 1.0 != 0.0) value else value.toInt()).toString()
}

fun formatNumbers(num: Int): String {
    return num.toString().replace("(?=(?!\\b)(\\d{3})+$)".toRegex(), ",")
}

fun colorOf(num: Int): Formatting {
    return when {
        num > 0 -> Formatting.GREEN
        num < 0 -> Formatting.RED
        else -> Formatting.DARK_GRAY
    }
}

fun colorOfDark(num: Int): Formatting {
    return when {
        num > 0 -> Formatting.DARK_GREEN
        num < 0 -> Formatting.DARK_RED
        else -> Formatting.GRAY
    }
}

fun playSound(sound: SoundEvent, pitch: Float = 1.0f) {
    MinecraftClient.getInstance().soundManager.play(PositionedSoundInstance.master(sound, pitch))
}

fun asRange(text: String): IRange = try {
    val array = text.split("-")
    SimpleIRange(array[0].toInt(), array[1].toInt())
}catch (e: Exception){
    SimpleIRange(0, 0)
}

fun asColor(text: String): Int {
    val color: Int = try {
        val array = text.split(",").toTypedArray()
        val r = array[0].toInt()
        val g = array[1].toInt()
        val b = array[2].toInt()
        (r shl 16) + (g shl 8) + b
    } catch (e: java.lang.Exception) {
        return -1
    }
    return color
}

fun tierOf(tier: Int): String {
    if (tier <= 0) return ""
    return when (tier){
        1 -> "I"
        2 -> "II"
        3 -> "III"
        4 -> "IV"
        5 -> "V"
        6 -> "VI"
        7 -> "VII"
        8 -> "VIII"
        9 -> "IX"
        10 -> "X"
        else -> "✰"
    }
}

fun formattingLines(text: String, prefix: String, length: Int = 200): List<Text> {
    val lines: MutableList<Text> = ArrayList()
    text.split("//").forEach {
        if(it == "") {
            lines.add(LiteralText.EMPTY)
        }else{
            warpLines(LiteralText(parseStyle(it, prefix)), length).forEach { line -> lines.add(line) }
        }
    }
    return lines
}

fun replaceProperty(text: String, prefix: Char, provider: Function<String, String>): String {
    val output = StringBuilder()
    var buffer: StringBuilder? = null
    var i = 0
    while (i < text.length) {
        val c = text[i]
        if (buffer == null) {
            if (c == prefix && i + 1 < text.length && text[i + 1] == '{') {
                buffer = StringBuilder()
                i += 1
            } else {
                output.append(c)
            }
        } else if (c == '}') {
            output.append(provider.apply(buffer.toString()))
            buffer = null
        } else {
            buffer.append(c)
        }
        i++
    }
    if (buffer != null) {
        output.append(buffer)
    }
    return output.toString()
}

fun warpLines(text: Text, length: Int): List<Text> {
    val visitor = StringVisitable.StyledVisitor<Text>{ style, asString ->
        Optional.of(LiteralText(asString).setStyle(style))
    }
    return MinecraftClient.getInstance().textRenderer.textHandler
        .wrapLines(text, length, Style.EMPTY)
        .mapNotNull { it.visit(visitor, Style.EMPTY).orElse(null) }
        .toList()
}

private val formattingChars: Set<Char> = setOf('0', '1', '2', '3', '4', '5', '6', '7',
    '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'k', 'l', 'm', 'n', 'o', 'r')

fun parseStyle(text: String, style: String): String {
    val buffer = StringBuilder(style)
    val stack = LinkedList<String>()
    var currentStyle = style
    var i = 0
    while (i < text.length) {
        val c = text[i]
        if (c == '}' && !stack.isEmpty()) {
            currentStyle = stack.pop()
            buffer.append(currentStyle)
        } else if (i < text.length - 1 && text[i + 1] == '{' && c in formattingChars) {
            stack.push(currentStyle)
            currentStyle = "§$c"
            buffer.append(currentStyle)
            i += 1
        } else {
            buffer.append(c)
        }
        i++
    }
    return buffer.toString()
}

fun <K, V> getKey(map: Map<K, V>, target: V): K? {
    for ((key, value) in map)
    {
        if (target == value) {
            return key
        }
    }
    return null
}