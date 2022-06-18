package io.github.nbcss.wynnlib.utils.range

class SimpleIRange(private val start: Int, private val end: Int): IRange {
    override fun upper(): Int = start
    override fun lower(): Int = end
}