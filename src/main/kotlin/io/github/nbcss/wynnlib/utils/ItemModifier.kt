package io.github.nbcss.wynnlib.utils

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtElement
import java.util.function.Function

object ItemModifier {
    const val KEY = "WynnLib"
    fun readInt(item: ItemStack, key: String): Int? {
        val nbt = item.nbt ?: return null
        val compound = nbt.getCompound(KEY) ?: return null
        return if (compound.contains(key)) {
            compound.getInt(key)
        }else{
            null
        }
    }

    fun putInt(item: ItemStack, key: String, value: Int) {
        val nbt = item.orCreateNbt
        val compound = nbt.getCompound(KEY)
        compound.putInt(key, value)
        nbt.put(KEY, compound)
        item.nbt = nbt
    }

    fun readDouble(item: ItemStack, key: String): Double? {
        val nbt = item.nbt ?: return null
        val compound = nbt.getCompound(KEY) ?: return null
        return if (compound.contains(key)) {
            compound.getDouble(key)
        }else{
            null
        }
    }

    fun putDouble(item: ItemStack, key: String, value: Double) {
        val nbt = item.orCreateNbt
        val compound = nbt.getCompound(KEY)
        compound.putDouble(key, value)
        nbt.put(KEY, compound)
        item.nbt = nbt
    }

    fun <T> getElement(item: ItemStack, key: String, f: Function<NbtElement, T?>): T? {
        val nbt = item.nbt ?: return null
        val compound = nbt.getCompound(KEY) ?: return null
        return if (compound.contains(key)) {
            f.apply(compound.get(key)!!)
        }else{
            null
        }
    }

    fun putElement(item: ItemStack, key: String, value: NbtElement) {
        val nbt = item.orCreateNbt
        val compound = nbt.getCompound(KEY)
        compound.put(key, value)
        nbt.put(KEY, compound)
        item.nbt = nbt
    }
}