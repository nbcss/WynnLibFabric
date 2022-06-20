package io.github.nbcss.wynnlib.abilities

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.item.ItemStack

class Archetype(json: JsonObject): Keyed {
    private val id: String
    private val name: String
    private val texture: ItemStack
    init {
        id = json["id"].asString
        name = json["name"].asString
        texture = ItemFactory.fromEncoding(json["texture"].asString)
    }

    fun getTexture(): ItemStack = texture

    override fun getKey(): String = id

}