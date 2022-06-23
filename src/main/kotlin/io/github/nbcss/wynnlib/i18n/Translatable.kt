package io.github.nbcss.wynnlib.i18n

import net.minecraft.text.TranslatableText

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
    fun translate(label: String? = null, vararg args: Any): TranslatableText {
        return TranslatableText(getTranslationKey(label), *args)
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