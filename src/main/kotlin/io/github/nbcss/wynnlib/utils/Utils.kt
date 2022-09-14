package io.github.nbcss.wynnlib.utils

import com.google.common.collect.Lists
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import it.unimi.dsi.fastutil.ints.Int2ObjectMap
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
import net.minecraft.client.MinecraftClient
import net.minecraft.client.sound.PositionedSoundInstance
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.sound.SoundEvent
import net.minecraft.text.LiteralText
import net.minecraft.text.StringVisitable
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.math.MathHelper
import java.util.*
import java.util.function.Function
import kotlin.math.roundToInt

private val client = MinecraftClient.getInstance()
const val BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

fun toBase64(value: Int): Char {
    return BASE64[MathHelper.clamp(value, 0, 63)]
}

fun fromBase64(value: Char): Int {
    return BASE64.indexOf(value)
}

fun writeClipboard(text: String) {
    client.keyboard.clipboard = text
}

fun readClipboard(): String? {
    return try {
        client.keyboard.clipboard
    }catch (e: Exception){
        null
    }
}

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

fun clearFormatting(text: String): String {
    return text.replace(Regex("§[0-9a-fklmnor]"), "")
}

fun formatTimer(time: Long): String {
    val t = if (time > 0) time / 1000 else 0
    if (t > 60 * 60 * 24) return "**:**"
    val min = if (t > 0) t / 60 else 0
    val sec = if (t > 0) t % 60 else 0
    return String.format("%02d:%02d", min, sec)
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

fun clickSlot(slotId: Int, button: Int, actionType: SlotActionType) {
    val handler = MinecraftClient.getInstance().player!!.currentScreenHandler
    val int2ObjectMap: Int2ObjectMap<ItemStack?> = Int2ObjectOpenHashMap()
    val defaultedList = handler.slots
    val i = defaultedList.size
    val list: MutableList<ItemStack> = Lists.newArrayListWithCapacity(i)
    for (slot in defaultedList) {
        list.add(slot.stack.copy())
    }
    for (j in 0 until i) {
        val itemStack2 = defaultedList[j].stack
        if (!ItemStack.areEqual(list[j], itemStack2)) {
            int2ObjectMap[j] = itemStack2.copy()
        }
    }
    val packet = ClickSlotC2SPacket(handler.syncId, handler.revision,
        slotId, button, actionType,
        handler.cursorStack.copy(), int2ObjectMap)
    MinecraftClient.getInstance().networkHandler!!.sendPacket(packet)
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