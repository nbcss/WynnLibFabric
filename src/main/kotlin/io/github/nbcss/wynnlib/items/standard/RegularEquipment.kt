package io.github.nbcss.wynnlib.items.standard

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.EquipmentType
import io.github.nbcss.wynnlib.data.Identification
import io.github.nbcss.wynnlib.data.Metadata
import io.github.nbcss.wynnlib.data.Metadata.asTier
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.items.Armour
import io.github.nbcss.wynnlib.items.Equipment
import io.github.nbcss.wynnlib.items.Weapon
import io.github.nbcss.wynnlib.utils.asIdentificationRange
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import java.util.function.Consumer

class RegularEquipment(json: JsonObject) : Equipment {
    private val idMap: MutableMap<Identification, Int> = LinkedHashMap()
    private val name: String
    private val displayName: String
    private val tier: Tier
    private val level: Int
    private val container: EquipmentContainer?
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
        val category = json.get("category").asString
        container = if(category.equals("weapon")){
            RegularWeapon(this, json)
        }else if(category.equals("armor")){
            RegularArmour(this, json)
        }else if(category.equals("accessory")){
            RegularAccessory(this, json)
        }else{
            null //hmm it should not happen right?
        }
    }

    override fun getTier(): Tier = tier

    override fun getIdentification(id: Identification): IntRange {
        return asIdentificationRange(idMap.getOrDefault(id, 0))
    }

    override fun getLevel(): IntRange = IntRange(level, level)

    override fun getType(): EquipmentType {
        return container!!.getType()
    }

    override fun getKey(): String = name

    override fun getDisplayName(): String = displayName

    override fun getIcon(): ItemStack = container!!.getIcon()

    override fun getColor(): Int {
        return Settings.getColor(tier.name)
    }

    override fun getTooltip(): List<Text> = container!!.getTooltip()

    override fun asWeapon(): Weapon? {
        return if(container is Weapon) container else null
    }

    override fun asArmour(): Armour? {
        return if(container is Armour) container else null
    }
}