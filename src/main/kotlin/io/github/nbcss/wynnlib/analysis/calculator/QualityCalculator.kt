package io.github.nbcss.wynnlib.analysis.calculator

import io.github.nbcss.wynnlib.i18n.SuffixTranslation
import io.github.nbcss.wynnlib.utils.colorOf
import io.github.nbcss.wynnlib.utils.range.BaseIRange
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

interface QualityCalculator {
    /**
     * Compute the quality of an identification value as 0.0 to 1.0 %.
     * If the quality is outside the range, indicate that the value is outdated.
     * The result can also be null if the range is constant.
     *
     * @param value, the roll value of the id
     * @param stars the number of stars of the id [0, 3]
     * @param range the id range, include the id instance
     */
    fun getQuality(value: Int, stars: Int, range: BaseIRange): Float?

    companion object {
        fun asQuality(value: Int, stars: Int, range: BaseIRange): Pair<Text, Float?> {
            val calculator = MaxRollCalculator
            val id = range.getIdentification()
            val color = colorOf(if (id.inverted) -range.upper() else range.upper())
            val text = SuffixTranslation.withSuffix(range.upper(), id.suffix).formatted(color)
            val quality = calculator.getQuality(value, stars, range)
            if (quality == null) return text to quality
            if (quality !in 0.0..1.0){
                text.append(LiteralText(" (Outdated)").formatted(Formatting.DARK_GRAY))
                return text to null
            }
            text.append(" ").append(formattingQuality(quality))
            return text to quality
        }

        fun formattingQuality(quality: Float): Text {
            val text = LiteralText("(%.1f%%)".format(quality * 100))
            if (quality >= 1.0) {
                text.formatted(Formatting.GOLD)
            }else if(quality >= 0.9) {
                text.formatted(Formatting.GREEN)
            }else if(quality >= 0.7) {
                text.formatted(Formatting.DARK_GREEN)
            }else if(quality >= 0.3) {
                text.formatted(Formatting.YELLOW)
            }else{
                text.formatted(Formatting.RED)
            }
            return text
        }
    }
}