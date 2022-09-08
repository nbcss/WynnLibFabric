package io.github.nbcss.wynnlib.gui.widgets.criteria

import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.gui.widgets.CheckboxWidget
import io.github.nbcss.wynnlib.items.equipments.Equipment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack

class ItemTypeGroup(memory: CriteriaMemory<Equipment>): CriteriaGroup<Equipment>(memory) {
    companion object {
        private val RENDER = MinecraftClient.getInstance().itemRenderer
        private const val FILTER_KEY = "ITEM_TYPE"
    }
    private val checkboxes: MutableMap<EquipmentType, CheckboxWidget> = linkedMapOf()
    private val height: Int
    init {
        var index = 0
        val range = listOf(1, 42, 83)
        val types = (memory.getFilter(FILTER_KEY) as? TypeFilter)?.types
        EquipmentType.getEquipmentTypes().forEach { type ->
            val posX = range[index % range.size]
            val posY = 2 + 20 * (index / range.size)
            checkboxes[type] = CheckboxWidget(posX, posY, types?.let { type in it } ?: true)
            index += 1
        }
        height = 4 + if (index % range.size == 0) {
            20 * (index / range.size)
        }else{
            20 * (1 + index / range.size)
        }
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, posX: Double, posY: Double, delta: Float) {
        for (entry in checkboxes.entries) {
            val widget = entry.value
            widget.updatePosition(posX.toInt(), posY.toInt())
            widget.render(matrices, mouseX, mouseY, delta)
            RENDER.renderInGui(entry.key.getIcon(), widget.x + 21, widget.y + 2)
        }
    }

    override fun reload(memory: CriteriaMemory<Equipment>) {
        memory.getFilter(FILTER_KEY)?.let {
            if (it is TypeFilter) {
                for (entry in checkboxes.entries) {
                    println("${entry.key}: ${entry.key in it.types}")
                    entry.value.setChecked(entry.key in it.types)
                }
            }
        }
    }

    override fun getHeight(): Int {
        return height
    }

    override fun onClick(mouseX: Int, mouseY: Int, button: Int): Boolean {
        if (checkboxes.values.any { it.mouseClicked(mouseX.toDouble(), mouseY.toDouble(), button) }){
            memory.putFilter(TypeFilter(checkboxes.entries
                .filter { it.value.isChecked() }
                .map { it.key }.toSet()))
            return true
        }
        return false
    }

    class TypeFilter(val types: Set<EquipmentType>): CriteriaMemory.Filter<Equipment> {

        override fun accept(item: Equipment): Boolean {
            return item.getType() in types
        }

        override fun getKey(): String = FILTER_KEY
    }
}