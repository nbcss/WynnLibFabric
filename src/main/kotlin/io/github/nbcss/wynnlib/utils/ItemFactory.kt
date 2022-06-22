package io.github.nbcss.wynnlib.utils

import io.github.nbcss.wynnlib.data.LegacyEntityMap
import net.minecraft.datafixer.fix.ItemIdFix
import io.github.nbcss.wynnlib.data.LegacyItemMap
import io.github.nbcss.wynnlib.mixins.datafixer.ItemInstanceSpawnEggFixAccessor
import io.github.nbcss.wynnlib.mixins.datafixer.ItemPotionFixAccessor
import io.github.nbcss.wynnlib.mixins.datafixer.RecipeFixAccessor
import net.minecraft.datafixer.fix.EntityTheRenameningBlock
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
        // todo the material Id of "Gert Bangswing Manypointystick" is 16387
        var itemName: String = LegacyItemMap.get(id) ?: ItemIdFix.fromId(id)
        var damage:Int = -1
        var potionType: String? = null

        itemName = EntityTheRenameningBlock.ITEMS[itemName] ?: itemName // I don't know why mojang likes to rename items so much
        val flattenedItemString = ItemInstanceTheFlatteningFix.getItem(itemName, meta)
        if (flattenedItemString != null) {
            itemName = flattenedItemString
        }
        else if (meta != 0){ // The item 'should' be in the ItemInstanceTheFlatteningFix.DAMAGEABLE_ITEMS
            damage = meta
        }
        itemName = RecipeFixAccessor.getRECIPES()[itemName] ?: itemName

        when(itemName){
            "minecraft:spawn_egg" -> run {
                if (meta == 0) {
                    return ItemStack(Items.WOLF_SPAWN_EGG)
                }
                val entity: String? = LegacyEntityMap.get(meta)
                if (entity != null) {
                    itemName = ItemInstanceSpawnEggFixAccessor.getEntitySpawnEggs()[entity].toString()
                }
            }
            "minecraft:potion", "minecraft:splash_potion", "minecraft:lingering_potion"-> run {
                potionType = ItemPotionFixAccessor.getID_TO_POTIONS()[meta]
            }
        }




        if (itemName != "minecraft:air") {
            val nbt = NbtCompound()
            val tag = NbtCompound()
            nbt.putString("id", itemName)
            nbt.putByte("Count", 1.toByte())
            if (potionType != null) {
                tag.putString("Potion", potionType)
            }
            if (damage != -1) {
                tag.putByte("Damage", damage.toByte())
                tag.putBoolean("Unbreakable", true)
            }
            nbt.put("tag", tag)
            val item = ItemStack.fromNbt(nbt)
            if (item.isEmpty){
                getLogger().warn("Could not find item with id $id and meta $meta")
                return ERROR_ITEM
            }
            return ItemStack.fromNbt(nbt)
        }
        getLogger().warn("Could not find item with id $id and meta $meta")
        return ERROR_ITEM
    }
}