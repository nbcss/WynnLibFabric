package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.Ingredient

object IngredientRegistry: Registry<Ingredient>() {
    private const val RESOURCE = "assets/wynnlib/data/Ingredients.json"
    private val nameMap: MutableMap<String, Ingredient> = LinkedHashMap()

    fun fromName(name: String): Ingredient? {
        return nameMap[name]
    }

    override fun getFilename(): String = RESOURCE

    override fun reload(array: JsonArray){
        nameMap.clear()
        super.reload(array)
    }

    override fun put(item: Ingredient) {
        nameMap[item.getDisplayName()] = item
        super.put(item)
    }

    override fun read(data: JsonObject): Ingredient? = try {
        Ingredient(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}