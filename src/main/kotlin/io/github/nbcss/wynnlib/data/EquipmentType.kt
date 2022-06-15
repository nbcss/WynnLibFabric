package io.github.nbcss.wynnlib.data

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.getItem
import io.github.nbcss.wynnlib.utils.getSkullItem
import net.minecraft.item.ItemStack

class EquipmentType(json: JsonObject): Keyed {
    val name: String = json.get("name").asString
    private val texture: ItemStack

    init {
        texture = if (json.has("iconSkin")) getSkullItem(json.get("iconSkin").asString)
        else getItem(json.get("icon").asString)
    }

    override fun getKey(): String = name

    fun getTexture(key: String): ItemStack {
        //val texture: ItemStack = textureMap.get(key.lowercase(Locale.getDefault()))
        //return texture ?: getIcon()
        return getIcon()
    }

    fun getIcon(): ItemStack {
        return texture
    }
}
