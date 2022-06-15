package io.github.nbcss.wynnlib.utils

import io.github.nbcss.wynnlib.WynnLibEntry
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.io.IOException

import java.io.InputStream

val ERROR_ITEM: ItemStack = ItemStack(Registry.ITEM.get(Identifier("barrier")))

fun asRange(text: String): IntRange = try {
    val array = text.split("-")
    IntRange(array[0].toInt(), array[1].toInt())
}catch (e: Exception){
    IntRange(0, 0)
}

fun getItemById(id: Int, meta: Int): ItemStack {
    val item: Item = Item.byRawId(id)
    if (item != Items.AIR) {
        //meta should write here
        val stack = ItemStack(item, 1)
        var nbt = stack.orCreateNbt
        val tag = if (nbt.contains("tag")) nbt.getCompound("tag") else NbtCompound()
        tag.putBoolean("Unbreakable", true)
        nbt.put("tag", tag)
        /*if (id == 383) {
            nbt = SpawnEggNames().fixTagCompound(nbt)
        }*/
        stack.writeNbt(nbt)
        return stack
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