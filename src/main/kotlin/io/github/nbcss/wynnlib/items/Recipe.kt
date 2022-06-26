package io.github.nbcss.wynnlib.items

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class Recipe(json: JsonObject): Keyed, BaseItem {
    private val id: String
    init {
        id = json["id"].asString
    }

    override fun getDisplayText(): Text {
        TODO("Not yet implemented")
    }

    override fun getDisplayName(): String {
        TODO("Not yet implemented")
    }

    override fun getIcon(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getRarityColor(): Color {
        TODO("Not yet implemented")
    }

    override fun getKey(): String {
        TODO("Not yet implemented")
    }
}