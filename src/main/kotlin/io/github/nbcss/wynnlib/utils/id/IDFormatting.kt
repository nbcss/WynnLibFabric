package io.github.nbcss.wynnlib.utils.id

import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.i18n.SuffixTranslation
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.items.identity.IdentificationHolder
import io.github.nbcss.wynnlib.utils.colorOf
import io.github.nbcss.wynnlib.utils.colorOfDark
import io.github.nbcss.wynnlib.utils.formattingLines
import io.github.nbcss.wynnlib.utils.range.IRange
import io.github.nbcss.wynnlib.utils.signed
import net.minecraft.text.Text
import net.minecraft.util.Formatting

enum class IDFormatting {
    BASE{
        override fun formatting(item: IdentificationHolder,
                                id: Identification, 
                                range: IRange): List<Text> {
            if (!range.isZero()){
                val color = colorOf(if (id.inverted) -range.lower() else range.lower())
                val text = SuffixTranslation.withSuffix(range.lower(), id.suffix).formatted(color)
                if (!range.isConstant()){
                    val nextColor = colorOf(if (id.inverted) -range.upper() else range.upper())
                    val rangeColor = colorOfDark(
                        when {
                            color != nextColor -> 0
                            id.inverted -> -range.lower()
                            else -> range.lower()
                        }
                    )
                    text.append(Translations.TOOLTIP_TO.formatted(rangeColor))
                    text.append(SuffixTranslation.withSuffix(range.upper(), id.suffix).formatted(nextColor))
                }
                return listOf(text.append(" ").append(id.getDisplayText(Formatting.GRAY, item)))
            }
            return emptyList()
        }
    },
    TOME{
        private val effect = Translatable.from("wynnlib.tooltip.tome.effect")
        override fun formatting(item: IdentificationHolder, id: Identification, range: IRange): List<Text> {
            if (!range.isZero()){
                var rangeText = signed(range.lower())
                if (!range.isConstant()){
                    rangeText += "-${range.upper()}"
                }
                val value = SuffixTranslation.withSuffix(rangeText, id.suffix).string + " " + id.translate().string
                val text = effect.translate(null, value).string
                return formattingLines(text, "${Formatting.GRAY}", 500)
            }
            return emptyList()
        }
    };

    companion object {
        fun fromName(name: String): IDFormatting {
            //only have two cases here so simple match :)
            return if (name.uppercase() == "TOME"){
                TOME
            }else{
                BASE
            }
        }
    }

    abstract fun formatting(item: IdentificationHolder, id: Identification, range: IRange): List<Text>
}