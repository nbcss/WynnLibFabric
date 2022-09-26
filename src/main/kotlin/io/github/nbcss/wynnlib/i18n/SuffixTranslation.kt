package io.github.nbcss.wynnlib.i18n

import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text

enum class SuffixTranslation(val suffix: String): Translatable {
    TIER(" tier"){
        override fun getTranslationKey(label: String?): String {
            return "wynnlib.tooltip.suffix.tier"
        }
    },
    PER_5S("/5s"){
        override fun getTranslationKey(label: String?): String {
            return "wynnlib.tooltip.suffix.per_5s"
        }
    },
    PER_3S("/3s"){
        override fun getTranslationKey(label: String?): String {
            return "wynnlib.tooltip.suffix.per_3s"
        }
    };

    companion object {
        private val suffixMap: Map<String, SuffixTranslation> = mapOf(
            pairs = values().map { it.suffix to it }.toTypedArray()
        )

        fun withSuffix(value: Int, suffix: String): MutableText {
            return withSuffix(signed(value), suffix)
        }

        fun withSuffix(value: String, suffix: String): MutableText {
            val translation = suffixMap[suffix]
            if (translation != null){
                return translation.translate(label = null, value)
            }
            return LiteralText(value + suffix)
        }
    }
}