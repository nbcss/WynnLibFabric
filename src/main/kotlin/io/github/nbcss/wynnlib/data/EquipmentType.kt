package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.lang.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.getItem
import net.minecraft.item.ItemStack
import java.util.*
import kotlin.collections.LinkedHashMap

enum class EquipmentType(val id: String,
                         iconName: String): Keyed, Translatable {
    BOW("Bow", "minecraft:bow"),
    SPEAR("Spear", "minecraft:iron_shovel"),
    WAND("Wand", "minecraft:stick"),
    DAGGER("Dagger", "minecraft:shears"),
    RELIK("Relik", "minecraft:stone_shovel#7"),
    HELMET("Helmet", "minecraft:diamond_helmet"),
    CHESTPLATE("Chestplate", "minecraft:diamond_chestplate"),
    LEGGINGS("Leggings", "minecraft:diamond_leggings"),
    BOOTS("Boots", "minecraft:diamond_boots"),
    RING("Ring", "minecraft:flint_and_steel#2"),
    NECKLACE("Necklace", "minecraft:flint_and_steel#19"),
    BRACELET("Bracelet", "minecraft:flint_and_steel#36"),
    INVALID("???", "minecraft:barrier");

    companion object {
        private val VALUE_MAP: MutableMap<String, EquipmentType> = LinkedHashMap()
        init {
            values().forEach { VALUE_MAP[it.name.lowercase(Locale.getDefault())] = it }
        }

        fun getEquipmentType(id: String): EquipmentType {
            return VALUE_MAP.getOrDefault(id.lowercase(Locale.getDefault()), INVALID)
        }
    }

    private val icon: ItemStack
    init {
        icon = getItem(iconName)
    }

    override fun getKey(): String = name

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.item_type.${getKey().lowercase(Locale.getDefault())}"
    }

    fun getTexture(key: String): ItemStack {
        //TODO
        //val texture: ItemStack = textureMap.get(key.lowercase(Locale.getDefault()))
        //return texture ?: getIcon()
        return getIcon()
    }

    fun getIcon(): ItemStack {
        return icon
    }
}
