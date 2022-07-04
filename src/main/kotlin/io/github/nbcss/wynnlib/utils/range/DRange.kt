package io.github.nbcss.wynnlib.utils.range

interface DRange: ValueRange<Double> {

    override fun isZero(): Boolean {
        return lower() == 0.0 && upper() == 0.0
    }

    companion object {
        val ZERO: DRange = object : DRange {
            override fun lower(): Double = 0.0
            override fun upper(): Double = 0.0
        }
    }
}