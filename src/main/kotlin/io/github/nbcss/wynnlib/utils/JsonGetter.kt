package io.github.nbcss.wynnlib.utils

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.util.function.Function

object JsonGetter {
    fun getOr(data: JsonObject, key: String, value: Boolean): Boolean {
        return if(data.has(key)) data[key].asBoolean else value
    }

    fun getOr(data: JsonObject, key: String, value: Int): Int {
        return if(data.has(key)) data[key].asInt else value
    }

    fun getOr(data: JsonObject, key: String, value: Double): Double {
        return if(data.has(key)) data[key].asDouble else value
    }

    fun getOr(data: JsonObject, key: String, value: String): String {
        return if(data.has(key)) data[key].asString else value
    }

    fun <T> getOr(data: JsonObject, key: String, value: List<T>, f: Function<JsonElement, T>): List<T> {
        return if(data.has(key)) data[key].asJsonArray.map { f.apply(it) } else value
    }

    fun <T> getOr(data: JsonObject, key: String, value: T, f: Function<JsonElement, T>): T {
        return if(data.has(key)) f.apply(data[key]) else value
    }
}