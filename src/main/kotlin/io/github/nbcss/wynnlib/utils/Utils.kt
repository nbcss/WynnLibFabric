package io.github.nbcss.wynnlib.utils

import io.github.nbcss.wynnlib.WynnLibEntry
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.StringNbtReader
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.io.IOException
import java.io.InputStream
import java.util.*
import kotlin.math.*

val ERROR_ITEM: ItemStack = ItemStack(Registry.ITEM.get(Identifier("barrier")))

fun signed(value: Int): String {
    return if(value <= 0) value.toString() else "+$value"
}

fun formatNumbers(num: Int): String {
    return num.toString().replace("(?=(?!\\b)(\\d{3})+$)".toRegex(), ",")
}

fun colorOf(num: Int): Formatting {
    return when {
        num > 0 -> Formatting.GREEN
        num < 0 -> Formatting.RED
        else -> Formatting.GRAY
    }
}

fun colorOfDark(num: Int): Formatting {
    return when {
        num > 0 -> Formatting.DARK_GREEN
        num < 0 -> Formatting.DARK_RED
        else -> Formatting.DARK_GRAY
    }
}

fun asRange(text: String): IRange = try {
    val array = text.split("-")
    IRange(array[0].toInt(), array[1].toInt())
}catch (e: Exception){
    IRange(0, 0)
}

fun asIdentificationRange(base: Int): IRange {
    if( base == 0 )return IRange(0,0)
    return if (base>0){
        IRange(max(1, base*0.3.roundToInt()), base*1.3.roundToInt())
    }
    else{
        IRange(base*1.3.roundToInt(), base*0.7.roundToInt())
    }
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

fun getSkullItem(skin: String?): ItemStack {
    val stack = ItemStack(Items.PLAYER_HEAD, 1)
    val tag = NbtCompound()
    val owner = NbtCompound()
    owner.putString("Id", UUID.randomUUID().toString())
    val prop = NbtCompound()
    val list = NbtList()
    val texture = NbtCompound()
    texture.putString("Value", skin)
    list.add(texture)
    prop.put("textures", list)
    owner.put("Properties", prop)
    tag.put("SkullOwner", owner)
    stack.writeNbt(tag)
    return stack
}

fun getItemById(id: Int, meta: Int): ItemStack {
    val item: Item = Item.byRawId(id)
    if (item != Items.AIR) {
        //todo meta should write here
        //DataFix may help?
        //ItemInstanceTheFlatteningFix
        val stack = ItemStack(item, 1)
        val nbt = stack.orCreateNbt
        val tag = if (nbt.contains("tag")) nbt.getCompound("tag") else NbtCompound()
        tag.putBoolean("Unbreakable", true)
        nbt.put("tag", tag)

        if (id == 383) {
            //nbt = ItemSpawnEggFix().fixTagCompound(nbt)
        }
        stack.writeNbt(nbt)
        return stack
    }
    return ERROR_ITEM
}

fun getItem(name: String): ItemStack {
    val array = name.split("#").toTypedArray()
    val item: Item = Registry.ITEM.get(Identifier(array[0]))
    try {
        if (item != Items.AIR) {
            val meta = if (array.size > 1) array[1].toInt() else 0
            val stack = ItemStack(item, 1)
            val tag = if (array.size > 2) StringNbtReader.parse(array[2]) else stack.orCreateNbt
            tag.putBoolean("Unbreakable", true)
            if (meta > 0){
                tag.putInt("Damage", meta)
            }
            stack.writeNbt(tag)
            return stack
        }
    } catch (ignore: java.lang.Exception) {
        ignore.printStackTrace()
    }
    return ERROR_ITEM
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