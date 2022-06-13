package io.github.nbcss.wynnlib.items.standard

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.Metadata.asTier
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.items.Armour
import io.github.nbcss.wynnlib.items.Equipment
import io.github.nbcss.wynnlib.items.Weapon
import net.minecraft.item.ItemStack
import net.minecraft.text.Text

class StandardEquipment(json: JsonObject) : Equipment {
    private val name: String
    private val displayName: String
    private val tier: Tier
    private val level: Int
    init {
        name = json.get("name").asString
        displayName = if (json.has("displayName")) json.get("displayName").asString else name
        tier = asTier(json.get("tier").asString)
        level = json.get("level").asInt
    }

    override fun getTier(): Tier = tier

    override fun getIdentification(id: Identification): IntRange {
        TODO("Not yet implemented")
    }

    override fun getLevel(): IntRange = IntRange(level, level)

    override fun getKey(): String = name

    override fun getDisplayName(): String = displayName

    override fun getIcon(): ItemStack {
        TODO("Not yet implemented")
    }

    override fun getColor(): Int {
        TODO("Not yet implemented")
    }

    override fun getTooltip(): List<Text> {
        TODO("Not yet implemented")
    }

    override fun asWeapon(): Weapon {
        TODO("Not yet implemented")
    }

    override fun asArmour(): Armour {
        TODO("Not yet implemented")
    }
}