package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.CraftedType
import io.github.nbcss.wynnlib.data.Recipe
import io.github.nbcss.wynnlib.items.Material

object RecipeRegistry: Registry<Recipe>() {
    private const val RESOURCE = "assets/wynnlib/data/Recipes.json"
    private val materialMap: MutableMap<String, MutableSet<Recipe>> = mutableMapOf()
    override fun getFilename(): String = RESOURCE

    override fun read(data: JsonObject): Recipe? = try {
        Recipe(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }

    override fun reload(array: JsonArray) {
        materialMap.clear()
        super.reload(array)
    }

    override fun put(item: Recipe) {
        super.put(item)
        item.getMaterials().forEach {
            materialMap.getOrPut(it.first){ mutableSetOf() }.add(item)
        }
    }

    fun fromMaterial(material: Material): Set<Recipe> {
        return materialMap[material.getName()] ?: emptySet()
    }

    /**
     * Get recipe from given (lower bound) level & type; could be null if there
     * is no such recipe for given level.
     */
    fun fromLevelType(level: Int, type: CraftedType): Recipe? {
        return itemMap.values.firstOrNull { it.getLevel().lower() == level && it.getType() == type }
    }
}