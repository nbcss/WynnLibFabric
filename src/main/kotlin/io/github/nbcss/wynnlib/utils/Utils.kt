package io.github.nbcss.wynnlib.utils

import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.client.MinecraftClient
import net.minecraft.text.LiteralText
import net.minecraft.text.StringVisitable
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.util.*

fun signed(value: Int): String {
    return if(value <= 0) value.toString() else "+$value"
}

fun signed(value: Double): String {
    return if(value <= 0.0) value.toString() else "+$value"
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

fun formattingLines(text: String, length: Int, prefix: String): List<Text> {
    val lines: MutableList<Text> = ArrayList()
    text.split("//").forEach {
        if(it == "") {
            lines.add(LiteralText.EMPTY)
        }else{
            warpLines(parseStyle(it, prefix), length).forEach { line -> lines.add(line) }
        }
    }
    return lines
}

fun replaceProperty(text: String, prefix: Char, provider: PropertyProvider): String {
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
            output.append(provider.getProperty(buffer.toString()))
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

fun warpLines(text: String, length: Int): List<Text> {
    val visitor = StringVisitable.StyledVisitor<Text>{ style, asString ->
        Optional.of(LiteralText(asString).setStyle(style))
    }
    return MinecraftClient.getInstance().textRenderer.textHandler
        .wrapLines(text, length, Style.EMPTY)
        .map { it.visit(visitor, Style.EMPTY).get() }
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

fun getLogger(): Logger {
    return LogManager.getLogger()
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