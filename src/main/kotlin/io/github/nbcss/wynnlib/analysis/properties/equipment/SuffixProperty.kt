package io.github.nbcss.wynnlib.analysis.properties.equipment

import io.github.nbcss.wynnlib.analysis.properties.AnalysisProperty
import io.github.nbcss.wynnlib.data.Tier
import net.minecraft.text.Text
import java.util.regex.Pattern

class SuffixProperty: AnalysisProperty {
    companion object {
        const val KEY = "SUFFIX"
    }
    private var tier: Tier? = null
    private var roll = 1

    fun getTier(): Tier? = tier

    fun getRoll(): Int = roll

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (tier != null || tooltip[line].siblings.isEmpty())
            return 0
        val base = tooltip[line].siblings[0]
        if(base.asString() == "")
            return 0
        val pattern = Pattern.compile("(.+) Item( \\[(\\d+)])?")
        val matcher = pattern.matcher(base.asString())
        if (matcher.find()){
            Tier.fromId(matcher.group(1))?.let {
                tier = it
                roll = matcher.group(3)?.toInt() ?: 1
                return 1
            }
        }
        return 0
    }

    override fun getKey(): String = KEY
}