package io.github.nbcss.wynnlib.lang

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Registry
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText

object Translator: Registry<TranslationKey>() {

    override fun read(data: JsonObject): TranslationKey = TranslationKey(data)

    fun asText(prefix: String, key: String): MutableText = asText(prefix, key, null)

    fun asText(prefix: String, key: String, label: String?): MutableText {
        val entryKey = asKey(prefix, key, label)
        val entry: TranslationKey? = get(entryKey)
        if(entry != null){
            return entry.getText()
        }
        return LiteralText(entryKey)
    }
}