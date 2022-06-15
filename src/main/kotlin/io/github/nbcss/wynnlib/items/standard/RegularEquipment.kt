package io.github.nbcss.wynnlib.items.standard

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.Metadata
import io.github.nbcss.wynnlib.data.Metadata.asTier
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.items.Armour
import io.github.nbcss.wynnlib.items.Equipment
import io.github.nbcss.wynnlib.items.Weapon
import io.github.nbcss.wynnlib.utils.getItemById
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import java.util.function.Consumer

class RegularEquipment(json: JsonObject) : Equipment {
    private val idMap: MutableMap<Identification, Int> = LinkedHashMap()
    private val name: String
    private val displayName: String
    private val tier: Tier
    private val level: Int
    private var stack: ItemStack? = null    //todo
    init {
        name = json.get("name").asString
        displayName = if (json.has("displayName")) json.get("displayName").asString else name
        tier = asTier(json.get("tier").asString)!!
        level = json.get("level").asInt
        Metadata.getIdentifications().filter{json.has(it.name)}.forEach(Consumer {
                val value = json.get(it.name).asInt
                if(value != 0)
                    idMap[it] = value
        })
        stack = getItemById(100, 0)
    }

    override fun getTier(): Tier = tier

    override fun getIdentification(id: Identification): IntRange {
        TODO("Not yet implemented")
    }

    override fun getLevel(): IntRange = IntRange(level, level)

    override fun getKey(): String = name

    override fun getDisplayName(): String = displayName

    override fun getIcon(): ItemStack {
        return stack!!
    }

    override fun getColor(): Int {
        return Settings.getColor(tier.name)
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