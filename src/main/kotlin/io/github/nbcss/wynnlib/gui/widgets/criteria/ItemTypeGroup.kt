package io.github.nbcss.wynnlib.gui.widgets.criteria

import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.gui.widgets.AdvanceSearchPaneWidget
import io.github.nbcss.wynnlib.gui.widgets.CheckboxWidget
import io.github.nbcss.wynnlib.items.equipments.Equipment
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack

class ItemTypeGroup(memory: CriteriaMemory<Equipment>): CriteriaGroup<Equipment>(memory) {
    companion object {
        private val RENDER = MinecraftClient.getInstance().itemRenderer
    }
    private val checkboxes: MutableMap<EquipmentType, CheckboxWidget> = linkedMapOf()
    private val height: Int
    init {
        var posX = 2
        var posY = 2
        EquipmentType.getEquipmentTypes().forEach {
            checkboxes[it] = CheckboxWidget(posX, posY)
            posX += 50
            if (posX + 50 > AdvanceSearchPaneWidget.SCROLL_WIDTH) {
                posX = 2
                posY += 20
            }
        }
        height = if (posX == 2) posY else (posY + 20)
    }

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, posX: Double, posY: Double, delta: Float) {
        for (entry in checkboxes.entries) {
            val widget = entry.value
            widget.updatePosition(posX.toInt(), posY.toInt())
            widget.render(matrices, mouseX, mouseY, delta)
            RENDER.renderInGui(entry.key.getIcon(), widget.x + 22, widget.y + 3)
        }
    }

    override fun reload(memory: CriteriaMemory<Equipment>) {

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

        override fun getKey(): String = "ITEM_TYPE"
    }
}