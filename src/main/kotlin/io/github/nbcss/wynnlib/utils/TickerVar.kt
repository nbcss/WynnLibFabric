package io.github.nbcss.wynnlib.utils

import kotlin.math.max

class TickerVar {
    private var target: Float = 0.0f
    private var pos: Float = 0.0f
    private var ticker: Int = -1
    fun tick() {
        ticker = max(-1, ticker - 1)

    }

    fun getCurrentPos(delta: Float) {

    }

    fun getRenderPos(delta: Float) {

    }

    fun setTargetPos(target: Float, tick: Int) {
        this.target = target
        this.ticker = tick
    }
}