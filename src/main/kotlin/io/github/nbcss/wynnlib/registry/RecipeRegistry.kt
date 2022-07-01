package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.Recipe

object RecipeRegistry: Registry<Recipe>() {
    override fun read(data: JsonObject): Recipe? = try {
        Recipe(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}