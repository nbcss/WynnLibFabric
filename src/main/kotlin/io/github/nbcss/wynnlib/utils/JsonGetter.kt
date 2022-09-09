package io.github.nbcss.wynnlib.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.function.Function

object JsonGetter {
    fun getOr(data: JsonObject, key: String, value: Boolean): Boolean {
        return if(data.has(key)) data[key].asBoolean else value
    }

    fun <T> getOr(data: JsonObject, key: String, value: List<T>, f: Function<JsonElement, T>): List<T> {
        return if(data.has(key)) data[key].asJsonArray.map { f.apply(it) } else value
    }
}