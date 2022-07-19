package io.github.nbcss.wynnlib.items.equipments.analysis.properties

import io.github.nbcss.wynnlib.data.PowderSpecial
import net.minecraft.text.Text
import java.util.regex.Pattern

class PowderSpecialProperty: AnalysisProperty {
    companion object {
        private val SPEC_NAME_PATTERN = Pattern.compile(" {3}(.+)")
        const val KEY = "POWDER_SPEC"
    }
    private var powderSpec: PowderSpecial? = null

    fun getPowderSpecial(): PowderSpecial? = powderSpec

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (tooltip[line].siblings.isEmpty())
            return 0
        val base = tooltip[line].siblings[0]
        val matcher = SPEC_NAME_PATTERN.matcher(base.asString())
        if(matcher.find()){
            val name = matcher.group(1)
            //TODO
        }
        return 0
    }

    override fun getKey(): String = KEY
}