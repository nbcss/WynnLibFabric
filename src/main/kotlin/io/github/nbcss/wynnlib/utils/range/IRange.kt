package io.github.nbcss.wynnlib.utils.range

interface IRange: ValueRange<Int> {
    companion object {
        val ZERO: IRange = object : IRange {
            override fun lower(): Int = 0
            override fun upper(): Int = 0
        }
    }
}