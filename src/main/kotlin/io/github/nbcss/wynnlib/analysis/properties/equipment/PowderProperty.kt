package io.github.nbcss.wynnlib.analysis.properties.equipment

import io.github.nbcss.wynnlib.analysis.properties.AnalysisProperty
import io.github.nbcss.wynnlib.data.Element
import net.minecraft.text.Text
import java.util.regex.Pattern

class PowderProperty: AnalysisProperty {
    companion object {
        private val POWDER_PATTERN = Pattern.compile("\\[\\d+/(\\d+)] Powder Slots")
        const val KEY = "POWDERS"
    }
    private var powderSlots: Int = 0
    private val powders: MutableList<Element> = mutableListOf()

    fun getPowderSlots(): Int = powderSlots

    fun getPowders(): List<Element> = powders

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (tooltip[line].siblings.isEmpty())
            return 0
        val base = tooltip[line].siblings[0]
        val baseString = base.asString()
        val matcher = if (baseString != "") {
            POWDER_PATTERN.matcher(baseString)
        }else if(base.siblings.isNotEmpty()){
            POWDER_PATTERN.matcher(base.siblings[0].asString())
        }else{
            return 0
        }
        if(matcher.find()) {
            powderSlots = matcher.group(1).toInt()
            powders.clear()
            for (i in (1 until base.siblings.size - 1)){
                Element.fromIcon(base.siblings[i].asString().trim())?.let {
                    powders.add(it)
                }
            }

            return 1
        }
        return 0
    }

    override fun getKey(): String = KEY
}