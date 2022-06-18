package io.github.nbcss.wynnlib.utils.range

import io.github.nbcss.wynnlib.data.Identification

class BaseIRange(private val id: Identification, private val base: Int): IRange {

    fun base(): Int = base

    override fun upper(): Int {
        //TODO
        return base()
    }

    override fun lower(): Int {
        //TODO
        return base()
    }
}