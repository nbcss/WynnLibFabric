package io.github.nbcss.wynnlib.utils

data class AlphaColor(val color: Color,
                      val alpha: Int) {
    fun code(): Int = color.code() + (alpha shl 24)

    fun floatRed(): Float = color.floatRed()

    fun floatGreen(): Float = color.floatGreen()

    fun floatBlue(): Float = color.floatBlue()

    fun floatAlpha(): Float = Color.normalize(alpha)
}