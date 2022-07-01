package io.github.nbcss.wynnlib.utils.range

interface DRange: ValueRange<Double> {
    companion object {
        val ZERO: DRange = object : DRange {
            override fun lower(): Double = 0.0
            override fun upper(): Double = 0.0
        }
    }
}