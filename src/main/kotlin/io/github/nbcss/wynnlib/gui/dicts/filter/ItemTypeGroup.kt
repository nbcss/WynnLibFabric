package io.github.nbcss.wynnlib.gui.dicts.filter

import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.gui.TooltipScreen
import io.github.nbcss.wynnlib.gui.widgets.scrollable.CheckboxWidget
import io.github.nbcss.wynnlib.gui.widgets.scrollable.ItemIconWidget
import io.github.nbcss.wynnlib.gui.widgets.scrollable.LabelWidget
import io.github.nbcss.wynnlib.items.equipments.Equipment
import io.github.nbcss.wynnlib.i18n.Translations.UI_FILTER_ITEM_TYPE
import net.minecraft.util.Formatting
import java.util.function.Supplier

class ItemTypeGroup(memory: CriteriaMemory<Equipment>,
                    private val screen: TooltipScreen): FilterGroup<Equipment>(memory) {
    companion object {
        private const val FILTER_KEY = "ITEM_TYPE"
    }
    private val checkboxes: MutableMap<EquipmentType, CheckboxWidget> = linkedMapOf()
    private val contentHeight: Int
    init {
        var index = 0
        val range = listOf(1, 42, 83)
        val types = (memory.getFilter(FILTER_KEY) as? TypeFilter)?.types
        addElement(LabelWidget(2, 2, Supplier {
            return@Supplier UI_FILTER_ITEM_TYPE.formatted(Formatting.GOLD)
        }, mode = LabelWidget.Mode.OUTLINE))
        EquipmentType.getEquipmentTypes().forEach { type ->
            val posX = range[index % range.size]
            val posY = 12 + 20 * (index / range.size)
            val name = type.formatted(Formatting.GRAY)
            val checkbox = CheckboxWidget(posX, posY, name, screen,
                types?.let { type in it } ?: true)
            checkbox.setCallback { updateFilter() }
            addElement(checkbox)
            addElement(ItemIconWidget(posX + 20, posY + 1, type.getIcon()))
            checkboxes[type] = checkbox
            index += 1
        }
        val group = CheckboxWidget.Group(checkboxes.values.toSet()) {
            updateFilter()
        }
        checkboxes.values.forEach { it.setGroup(group) }
        contentHeight = 10 + if (index % range.size == 0) {
            20 * (index / range.size)
        }else{
            20 * (1 + index / range.size)
        }
    }

    private fun updateFilter() {
        memory.putFilter(TypeFilter(checkboxes.entries
            .filter { it.value.isChecked() }
            .map { it.key }.toSet()))
    }

    override fun getHeight(): Int {
        return contentHeight
    }

    /*override fun renderContent(
        matrices: MatrixStack,
        mouseX: Int,
        mouseY: Int,
        posX: Double,
        posY: Double,
        delta: Float,
        mouseOver: Boolean
    ) {
        val x = floor(posX).toInt()
        val y = floor(posY).toInt()
        for (entry in checkboxes.entries) {
            val widget = entry.value
            widget.updateState(x, y, mouseOver)
            widget.render(matrices, mouseX, mouseY, delta)
            RENDER.renderInGui(entry.key.getIcon(), widget.x + 20, widget.y + 1)
        }
    }*/

    override fun reload(memory: CriteriaMemory<Equipment>) {
        memory.getFilter(FILTER_KEY)?.let {
            if (it is TypeFilter) {
                for (entry in checkboxes.entries) {
                    entry.value.setChecked(entry.key in it.types)
                }
            }
        }
    }

    //override fun getTitle(): Text = UI_FILTER_ITEM_TYPE.formatted(Formatting.GOLD)

    //override fun getContentHeight(): Int = contentHeight

    class TypeFilter(val types: Set<EquipmentType>): CriteriaMemory.Filter<Equipment> {

        override fun accept(item: Equipment): Boolean {
            return item.getType() in types
        }

        override fun getKey(): String = FILTER_KEY
    }
}