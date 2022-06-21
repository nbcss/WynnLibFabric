package io.github.nbcss.wynnlib.utils

import io.github.nbcss.wynnlib.WynnLibEntry
import io.github.nbcss.wynnlib.utils.ItemFactory.fromLegacyId
import io.github.nbcss.wynnlib.mixins.datafixer.EntityBlockStateFixAccessor
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.range.SimpleIRange
import net.minecraft.datafixer.fix.ItemIdFix
import net.minecraft.datafixer.fix.ItemInstanceTheFlatteningFix
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.StringNbtReader
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.slf4j.Logger
import java.io.IOException
import java.io.InputStream
import java.util.*

val ERROR_ITEM: ItemStack = ItemStack(Registry.ITEM.get(Identifier("barrier")))

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

fun getResource(filename: String): InputStream? {
    return try {
        val url = WynnLibEntry.javaClass.classLoader.getResource(filename)
        if (url == null) {
            null
        } else {
            val connection = url.openConnection()
            connection.useCaches = false
            connection.getInputStream()
        }
    } catch (var4: IOException) {
        null
    }
}

fun getLogger():Logger{
    return com.mojang.logging.LogUtils.getLogger()
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