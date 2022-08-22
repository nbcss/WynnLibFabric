package io.github.nbcss.wynnlib.analysis.properties

import net.minecraft.text.Text
import java.util.regex.Pattern

class MarketPriceProperty: AnalysisProperty {
    companion object {
        private val PRICE_PATTERN = Pattern.compile("\\[\\d+/(\\d+)] Powder Slots")
        const val KEY = "MARKET_PRICE"
    }

    override fun set(tooltip: List<Text>, line: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getKey(): String = KEY
}