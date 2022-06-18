package io.github.nbcss.wynnlib.utils.range

class IngredientIRange(private val min: Int, private val max: Int): IRange {
    override fun lower(): Int = min
    override fun upper(): Int = max
}