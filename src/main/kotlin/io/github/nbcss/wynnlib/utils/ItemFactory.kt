package io.github.nbcss.wynnlib.utils

import net.minecraft.datafixer.fix.ItemInstanceTheFlatteningFix
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.StringNbtReader
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import java.util.*

object ItemFactory {
    //todo add 176 to 255 (blocks), 423 to 453 (items)
    //https://minecraft.fandom.com/wiki/Java_Edition_data_values/Pre-flattening#Block_IDs
    private val legacyMap: Map<Int, String> = mapOf(
        409 to "minecraft:prismarine_shard",
        410 to "minecraft:prismarine_crystals",
        411 to "minecraft:rabbit",
        412 to "minecraft:cooked_rabbit",
        413 to "minecraft:rabbit_stew",
        414 to "minecraft:rabbit_foot",
        415 to "minecraft:rabbit_hide",
        416 to "minecraft:armor_stand",
    )
    val ERROR_ITEM: ItemStack = ItemStack(Registry.ITEM.get(Identifier("barrier")))

    /**
     * Get skull item from given skin string.
     */
    fun fromSkin(skin: String): ItemStack {
        val stack = ItemStack(Items.PLAYER_HEAD, 1)
        val tag = NbtCompound()
        val owner = NbtCompound()
        val properties = NbtCompound()
        val textures = NbtList()
        val texture = NbtCompound()
        texture.putString("Value", skin)
        textures.add(texture)
        properties.put("textures", textures)
        owner.putUuid("Id", UUID.randomUUID())
        owner.put("Properties", properties)
        tag.put("SkullOwner", owner)
        stack.nbt = tag
        return stack
    }

    /**
     * Get ItemStack from given encoding.
     * Encoding is in the form of ID#DAMAGE#NBT, where ID is the minecraft identifier
     * (such as "minecraft:stone"), damage is the damage value for damageable items,
     * and NBT is more specific item with nbt compound (such as potion). DAMAGE and NBT
     * are optional, if not specified the method will assume 0 damage with default nbt.
     *
     * Note that this method assuming use flatten id, and will not do flatten in decoding.
     *
     * @param encoding the encoding of the item
     * @return item for associated encoding, or ERROR_ITEM if there is an error.
     */
    fun fromEncoding(encoding: String): ItemStack {
        val array = encoding.split("#").toTypedArray()
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
                stack.nbt = tag
                return stack
            }
        } catch (ignore: java.lang.Exception) {
            ignore.printStackTrace()
        }
        return ERROR_ITEM
    }

    /**
     * Get item from legacy id and meta (data value).
     * This method will also perform flatten for the numeric id.
     * Currently, incomplete but should support spawn egg flatten in the future.
     *
     * @param id numeric id (legacy) of the item.
     * @param meta data value of the item (0 for most items without variant)
     * @return item associated to given id & meta value.
     */
    fun fromLegacyId(id: Int, meta: Int): ItemStack {
        var itemId: String? = legacyMap[id]
        if (itemId == null){
            itemId = net.minecraft.datafixer.fix.ItemIdFix.fromId(id)
        }
        var damage = meta
        val flatten = ItemInstanceTheFlatteningFix.getItem(itemId, meta)
        if (flatten != null) {
            itemId = flatten
            damage = 0
        }
        // todo fix SpawnEgg
        //println("$id:$meta -> $itemId#$damage")
        return if (itemId == "minecraft.air") ERROR_ITEM else fromEncoding("$itemId#$damage")
    }
}