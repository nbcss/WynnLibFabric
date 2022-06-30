package io.github.nbcss.wynnlib.utils.range

data class SimpleIRange(private val lower: Int, private val upper: Int): IRange {
    override fun lower(): Int = lower
    override fun upper(): Int = upper
}