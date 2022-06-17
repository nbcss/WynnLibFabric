package io.github.nbcss.wynnlib.gui

import io.github.nbcss.wynnlib.items.Equipment
import io.github.nbcss.wynnlib.lang.Translatable.Companion.from
import io.github.nbcss.wynnlib.registry.RegularEquipmentRegistry
import net.minecraft.text.TranslatableText

class EquipmentDictScreen : DictionaryScreen<Equipment>(from("wynnlib.ui.equipments").translate()) {

    override fun fetchItems(): Collection<Equipment> {
        return RegularEquipmentRegistry.getAll()
    }
}