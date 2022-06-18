package io.github.nbcss.wynnlib.items

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.BaseItem
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class Ingredient(json: JsonObject) : Keyed, BaseItem {
    init {

    }

    override fun getDisplayText(): Text {
        TODO("Not yet implemented")
    }

    override fun getIcon(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getRarityColor(): Int {
        TODO("Not yet implemented")
    }

    override fun getTooltip(): List<Text> {
        TODO("Not yet implemented")
    }

    override fun getKey(): String {
        TODO("Not yet implemented")
    }
}