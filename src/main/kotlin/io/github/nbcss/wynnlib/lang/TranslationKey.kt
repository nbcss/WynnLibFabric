package io.github.nbcss.wynnlib.lang

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.translate
import net.minecraft.text.TranslatableText
import java.util.*

class TranslationKey(json: JsonObject): Keyed {
    private val entryKey: String
    private val translationKey: String
    init {
        val prefix = json.get("prefix").asString
        val key = json.get("key").asString
        val label = if(json.has("label")) json.get("label").asString else null
        entryKey = asKey(prefix, key, label)
        translationKey = json.get("translation").asString
    }
    override fun getKey(): String = entryKey

    fun getText(): TranslatableText {
        return translate(translationKey)
    }
}

fun asKey(prefix: String, key: String, label: String?): String{
    return ("$prefix.$key" + if(label == null) "" else (".$label")).lowercase(Locale.getDefault())
}