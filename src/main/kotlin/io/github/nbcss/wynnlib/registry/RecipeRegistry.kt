package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.CraftedType
import io.github.nbcss.wynnlib.data.Recipe

object RecipeRegistry: Registry<Recipe>() {
    private const val RESOURCE = "assets/wynnlib/data/Recipes.json"
    override fun getFilename(): String = RESOURCE

    override fun read(data: JsonObject): Recipe? = try {
        Recipe(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }

    /**
     * Get recipe from given (lower bound) level & type; could be null if there
     * is no such recipe for given level.
     */
    fun fromLevelType(level: Int, type: CraftedType): Recipe? {
        return itemMap.values.firstOrNull { it.getLevel().lower() == level && it.getType() == type }
    }
}