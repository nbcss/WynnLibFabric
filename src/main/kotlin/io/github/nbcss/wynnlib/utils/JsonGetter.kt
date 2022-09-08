package io.github.nbcss.wynnlib.utils

import com.google.gson.JsonObject

object JsonGetter {
    fun getOr(data: JsonObject, key: String, value: Boolean): Boolean {
        return if(data.has(key)) data[key].asBoolean else value
    }
}