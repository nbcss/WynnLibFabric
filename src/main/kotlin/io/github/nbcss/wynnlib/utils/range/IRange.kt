package io.github.nbcss.wynnlib.utils.range

interface IRange {
    fun upper(): Int
    fun lower(): Int
    fun isZero(): Boolean = upper() == 0 && lower() == 0
    fun isConstant(): Boolean = upper() == lower()

    companion object {
        val ZERO: IRange = object : IRange {
            override fun upper(): Int = 0
            override fun lower(): Int = 0
        }
    }
}