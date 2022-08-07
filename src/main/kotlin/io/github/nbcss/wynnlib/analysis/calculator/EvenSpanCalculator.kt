package io.github.nbcss.wynnlib.analysis.calculator

import io.github.nbcss.wynnlib.utils.range.BaseIRange
import net.minecraft.util.math.MathHelper
import kotlin.math.max
import kotlin.math.min

object EvenSpanCalculator: QualityCalculator {
    override fun getQuality(value: Int, stars: Int, range: BaseIRange): Float? {
        if (value < min(range.lower(), range.upper()))
            return -1.0f //outdated range
        if (value > max(range.lower(), range.upper()))
            return -1.0f //outdated range
        if (range.isConstant())
            return null //fixed value, no need to compute quality
        val quality = (value - range.lower()).toFloat() / (range.upper() - range.lower()).toFloat()
        return MathHelper.clamp(if (quality == 0.0f) 0.0f else quality, 0.0f, 1.0f)
    }
}