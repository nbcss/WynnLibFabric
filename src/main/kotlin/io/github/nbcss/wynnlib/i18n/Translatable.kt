package io.github.nbcss.wynnlib.i18n

import io.github.nbcss.wynnlib.utils.parseStyle
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting

interface Translatable {
    /**
     * Get translation key for given label (which further specify what item for translate).
     *
     * @param label the optional label for get translation key, default is null
     * @return the translation key
     */
    fun getTranslationKey(label: String? = null): String

    /**
     * Translate given label & var args to TranslatableText.
     */
    fun translate(label: String? = null, vararg args: Any): MutableText {
        return TranslatableText(getTranslationKey(label), *args)
    }

    /**
     * Translate given label & var args to TranslatableText, while applying formatting parse.
     */
    fun formatted(style: String = "", label: String? = null, vararg args: Any): MutableText {
        return LiteralText(parseStyle(translate(label, *args).string, style))
    }

    /**
     * Translate given label & var args to TranslatableText, while applying formatting parse.
     */
    fun formatted(style: Formatting, label: String? = null, vararg args: Any): MutableText {
        return formatted(style.toString(), label, *args).formatted(style)
    }

    companion object {
        /**
         * Get Translatable instance from translation key.
         */
        fun from(key: String): Translatable {
            return object : Translatable {
                override fun getTranslationKey(label: String?): String = key
            }
        }
    }
}