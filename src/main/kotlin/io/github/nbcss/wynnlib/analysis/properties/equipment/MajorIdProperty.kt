package io.github.nbcss.wynnlib.analysis.properties.equipment

import io.github.nbcss.wynnlib.analysis.properties.AnalysisProperty
import io.github.nbcss.wynnlib.data.MajorId
import io.github.nbcss.wynnlib.items.equipments.MajorIdContainer
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.util.Formatting
import java.util.regex.Pattern

class MajorIdProperty: AnalysisProperty {
    companion object {
        private val MAJOR_ID_PATTERN = Pattern.compile("^\\+(.+): $")
        const val KEY = "MAJOR_ID"
    }
    private val majorIds: MutableList<MajorId> = mutableListOf()
    private val containers: MutableList<MajorIdContainer> = mutableListOf()

    fun getMajorIds(): List<MajorId> = majorIds

    fun getMajorIdContainers(): List<MajorIdContainer> = containers

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (tooltip[line].siblings.isEmpty())
            return 0
        val base = tooltip[line].siblings[0]
        if (base.siblings.isEmpty())
            return 0
        if (base.siblings[0].style.color == TextColor.fromFormatting(Formatting.AQUA)) {
            val matcher = MAJOR_ID_PATTERN.matcher(base.siblings[0].asString())
            if (matcher.find()) {
                MajorId.fromDisplayName(matcher.group(1))?.let {
                    majorIds.add(it)
                    val majorIdTooltip = mutableListOf(tooltip[line])
                    for(i in (line + 1 until tooltip.size)) {
                        if (tooltip[i].asString() == ""
                            && tooltip[i].siblings.isNotEmpty()
                            && tooltip[i].siblings[0].style.color == TextColor.fromFormatting(Formatting.DARK_AQUA)){
                            majorIdTooltip.add(tooltip[i])
                        }else{
                            break
                        }
                    }
                    containers.add(MajorIdContainer(it, majorIdTooltip))
                    return majorIdTooltip.size
                }
            }
        }
        return 0
    }

    override fun getKey(): String = KEY
}