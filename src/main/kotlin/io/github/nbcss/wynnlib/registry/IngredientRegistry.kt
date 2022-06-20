package io.github.nbcss.wynnlib.registry

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.Ingredient

object IngredientRegistry: Registry<Ingredient>() {
    override fun read(data: JsonObject): Ingredient? = try {
        Ingredient(data)
    }catch (e: Exception){
        e.printStackTrace()
        null
    }
}