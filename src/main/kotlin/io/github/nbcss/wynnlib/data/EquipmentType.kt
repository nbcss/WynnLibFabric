package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.ERROR_ITEM
import io.github.nbcss.wynnlib.utils.ItemFactory
import io.github.nbcss.wynnlib.utils.Keyed
import net.minecraft.item.ItemStack
import java.util.*
import kotlin.collections.LinkedHashMap

enum class EquipmentType(private val id: String,
                         private val icon: ItemStack,
                         private val textureMap: Map<String, ItemStack>
                         ): Keyed, Translatable {
    BOW("Bow", ItemFactory.fromEncoding("minecraft:bow"), mapOf(
        "bow_basic_wood" to ItemFactory.fromEncoding("minecraft:bow"),
        "bow_basic_gold" to ItemFactory.fromEncoding("minecraft:bow#1"),
        "bow_earth_a" to ItemFactory.fromEncoding("minecraft:bow#2"),
        "bow_earth_b" to ItemFactory.fromEncoding("minecraft:bow#3"),
        "bow_earth_c" to ItemFactory.fromEncoding("minecraft:bow#4"),
        "bow_thunder_a" to ItemFactory.fromEncoding("minecraft:bow#5"),
        "bow_thunder_b" to ItemFactory.fromEncoding("minecraft:bow#6"),
        "bow_thunder_c" to ItemFactory.fromEncoding("minecraft:bow#7"),
        "bow_fire_a" to ItemFactory.fromEncoding("minecraft:bow#8"),
        "bow_fire_b" to ItemFactory.fromEncoding("minecraft:bow#9"),
        "bow_fire_c" to ItemFactory.fromEncoding("minecraft:bow#10"),
        "bow_air_a" to ItemFactory.fromEncoding("minecraft:bow#11"),
        "bow_air_b" to ItemFactory.fromEncoding("minecraft:bow#12"),
        "bow_air_c" to ItemFactory.fromEncoding("minecraft:bow#13"),
        "bow_water_a" to ItemFactory.fromEncoding("minecraft:bow#14"),
        "bow_water_b" to ItemFactory.fromEncoding("minecraft:bow#15"),
        "bow_water_c" to ItemFactory.fromEncoding("minecraft:bow#16"),
        "bow_multi_a" to ItemFactory.fromEncoding("minecraft:bow#17"),
        "bow_multi_b" to ItemFactory.fromEncoding("minecraft:bow#18"),
        "bow_multi_c" to ItemFactory.fromEncoding("minecraft:bow#19")
    )),
    SPEAR("Spear", ItemFactory.fromEncoding("minecraft:iron_shovel"), mapOf(
        "spear_basic_wood" to ItemFactory.fromEncoding("minecraft:iron_shovel"),
        "spear_basic_gold" to ItemFactory.fromEncoding("minecraft:iron_shovel#1"),
        "spear_earth_a" to ItemFactory.fromEncoding("minecraft:iron_shovel#2"),
        "spear_earth_b" to ItemFactory.fromEncoding("minecraft:iron_shovel#3"),
        "spear_earth_c" to ItemFactory.fromEncoding("minecraft:iron_shovel#4"),
        "spear_thunder_a" to ItemFactory.fromEncoding("minecraft:iron_shovel#5"),
        "spear_thunder_b" to ItemFactory.fromEncoding("minecraft:iron_shovel#6"),
        "spear_thunder_c" to ItemFactory.fromEncoding("minecraft:iron_shovel#7"),
        "spear_fire_a" to ItemFactory.fromEncoding("minecraft:iron_shovel#8"),
        "spear_fire_b" to ItemFactory.fromEncoding("minecraft:iron_shovel#9"),
        "spear_fire_c" to ItemFactory.fromEncoding("minecraft:iron_shovel#10"),
        "spear_air_a" to ItemFactory.fromEncoding("minecraft:iron_shovel#11"),
        "spear_air_b" to ItemFactory.fromEncoding("minecraft:iron_shovel#12"),
        "spear_air_c" to ItemFactory.fromEncoding("minecraft:iron_shovel#13"),
        "spear_water_a" to ItemFactory.fromEncoding("minecraft:iron_shovel#14"),
        "spear_water_b" to ItemFactory.fromEncoding("minecraft:iron_shovel#15"),
        "spear_water_c" to ItemFactory.fromEncoding("minecraft:iron_shovel#16"),
        "spear_multi_a" to ItemFactory.fromEncoding("minecraft:iron_shovel#17"),
        "spear_multi_b" to ItemFactory.fromEncoding("minecraft:iron_shovel#18"),
        "spear_multi_c" to ItemFactory.fromEncoding("minecraft:iron_shovel#19")
    )),
    WAND("Wand", ItemFactory.fromEncoding("minecraft:stick"), mapOf(
        "wand_basic_wood" to ItemFactory.fromEncoding("minecraft:stick"),
        "wand_basic_gold" to ItemFactory.fromEncoding("minecraft:wooden_shovel#1"),
        "wand_earth_a" to ItemFactory.fromEncoding("minecraft:wooden_shovel#2"),
        "wand_earth_b" to ItemFactory.fromEncoding("minecraft:wooden_shovel#3"),
        "wand_earth_c" to ItemFactory.fromEncoding("minecraft:wooden_shovel#4"),
        "wand_thunder_a" to ItemFactory.fromEncoding("minecraft:wooden_shovel#5"),
        "wand_thunder_b" to ItemFactory.fromEncoding("minecraft:wooden_shovel#6"),
        "wand_thunder_c" to ItemFactory.fromEncoding("minecraft:wooden_shovel#7"),
        "wand_fire_a" to ItemFactory.fromEncoding("minecraft:wooden_shovel#8"),
        "wand_fire_b" to ItemFactory.fromEncoding("minecraft:wooden_shovel#9"),
        "wand_fire_c" to ItemFactory.fromEncoding("minecraft:wooden_shovel#10"),
        "wand_air_a" to ItemFactory.fromEncoding("minecraft:wooden_shovel#11"),
        "wand_air_b" to ItemFactory.fromEncoding("minecraft:wooden_shovel#12"),
        "wand_air_c" to ItemFactory.fromEncoding("minecraft:wooden_shovel#13"),
        "wand_water_a" to ItemFactory.fromEncoding("minecraft:wooden_shovel#14"),
        "wand_water_b" to ItemFactory.fromEncoding("minecraft:wooden_shovel#15"),
        "wand_water_c" to ItemFactory.fromEncoding("minecraft:wooden_shovel#16"),
        "wand_multi_a" to ItemFactory.fromEncoding("minecraft:wooden_shovel#17"),
        "wand_multi_b" to ItemFactory.fromEncoding("minecraft:wooden_shovel#18"),
        "wand_multi_c" to ItemFactory.fromEncoding("minecraft:wooden_shovel#19")
    )),
    DAGGER("Dagger", ItemFactory.fromEncoding("minecraft:shears"), mapOf(
        "dagger_basic_wood" to ItemFactory.fromEncoding("minecraft:shears"),
        "dagger_basic_gold" to ItemFactory.fromEncoding("minecraft:shears#1"),
        "dagger_earth_a" to ItemFactory.fromEncoding("minecraft:shears#2"),
        "dagger_earth_b" to ItemFactory.fromEncoding("minecraft:shears#3"),
        "dagger_earth_c" to ItemFactory.fromEncoding("minecraft:shears#4"),
        "dagger_thunder_a" to ItemFactory.fromEncoding("minecraft:shears#5"),
        "dagger_thunder_b" to ItemFactory.fromEncoding("minecraft:shears#6"),
        "dagger_thunder_c" to ItemFactory.fromEncoding("minecraft:shears#7"),
        "dagger_fire_a" to ItemFactory.fromEncoding("minecraft:shears#8"),
        "dagger_fire_b" to ItemFactory.fromEncoding("minecraft:shears#9"),
        "dagger_fire_c" to ItemFactory.fromEncoding("minecraft:shears#10"),
        "dagger_air_a" to ItemFactory.fromEncoding("minecraft:shears#11"),
        "dagger_air_b" to ItemFactory.fromEncoding("minecraft:shears#12"),
        "dagger_air_c" to ItemFactory.fromEncoding("minecraft:shears#13"),
        "dagger_water_a" to ItemFactory.fromEncoding("minecraft:shears#14"),
        "dagger_water_b" to ItemFactory.fromEncoding("minecraft:shears#15"),
        "dagger_water_c" to ItemFactory.fromEncoding("minecraft:shears#16"),
        "dagger_multi_a" to ItemFactory.fromEncoding("minecraft:shears#17"),
        "dagger_multi_b" to ItemFactory.fromEncoding("minecraft:shears#18"),
        "dagger_multi_c" to ItemFactory.fromEncoding("minecraft:shears#19")
    )),
    RELIK("Relik", ItemFactory.fromEncoding("minecraft:stone_shovel#7"), mapOf(
        "relic_basic_wood" to ItemFactory.fromEncoding("minecraft:stone_shovel#7"),
        "relic_basic_gold" to ItemFactory.fromEncoding("minecraft:stone_shovel#8"),
        "relic_earth_a" to ItemFactory.fromEncoding("minecraft:stone_shovel#9"),
        "relic_earth_b" to ItemFactory.fromEncoding("minecraft:stone_shovel#10"),
        "relic_earth_c" to ItemFactory.fromEncoding("minecraft:stone_shovel#11"),
        "relic_thunder_a" to ItemFactory.fromEncoding("minecraft:stone_shovel#12"),
        "relic_thunder_b" to ItemFactory.fromEncoding("minecraft:stone_shovel#13"),
        "relic_thunder_c" to ItemFactory.fromEncoding("minecraft:stone_shovel#14"),
        "relic_fire_a" to ItemFactory.fromEncoding("minecraft:stone_shovel#15"),
        "relic_fire_b" to ItemFactory.fromEncoding("minecraft:stone_shovel#16"),
        "relic_fire_c" to ItemFactory.fromEncoding("minecraft:stone_shovel#17"),
        "relic_air_a" to ItemFactory.fromEncoding("minecraft:stone_shovel#18"),
        "relic_air_b" to ItemFactory.fromEncoding("minecraft:stone_shovel#19"),
        "relic_air_c" to ItemFactory.fromEncoding("minecraft:stone_shovel#20"),
        "relic_water_a" to ItemFactory.fromEncoding("minecraft:stone_shovel#21"),
        "relic_water_b" to ItemFactory.fromEncoding("minecraft:stone_shovel#22"),
        "relic_water_c" to ItemFactory.fromEncoding("minecraft:stone_shovel#23"),
        "relic_multi_a" to ItemFactory.fromEncoding("minecraft:stone_shovel#24"),
        "relic_multi_b" to ItemFactory.fromEncoding("minecraft:stone_shovel#25"),
        "relic_multi_c" to ItemFactory.fromEncoding("minecraft:stone_shovel#26")
    )),
    HELMET("Helmet", ItemFactory.fromEncoding("minecraft:diamond_helmet"), mapOf(
        "leather" to ItemFactory.fromEncoding("minecraft:leather_helmet"),
        "chain" to ItemFactory.fromEncoding("minecraft:chainmail_helmet"),
        "iron" to ItemFactory.fromEncoding("minecraft:iron_helmet"),
        "golden" to ItemFactory.fromEncoding("minecraft:golden_helmet"),
        "diamond" to ItemFactory.fromEncoding("minecraft:diamond_helmet")
    )),
    CHESTPLATE("Chestplate", ItemFactory.fromEncoding("minecraft:diamond_chestplate"), mapOf(
        "leather" to ItemFactory.fromEncoding("minecraft:leather_chestplate"),
        "chain" to ItemFactory.fromEncoding("minecraft:chainmail_chestplate"),
        "iron" to ItemFactory.fromEncoding("minecraft:iron_chestplate"),
        "golden" to ItemFactory.fromEncoding("minecraft:golden_chestplate"),
        "diamond" to ItemFactory.fromEncoding("minecraft:diamond_chestplate")
    )),
    LEGGINGS("Leggings", ItemFactory.fromEncoding("minecraft:diamond_leggings"), mapOf(
        "leather" to ItemFactory.fromEncoding("minecraft:leather_leggings"),
        "chain" to ItemFactory.fromEncoding("minecraft:chainmail_leggings"),
        "iron" to ItemFactory.fromEncoding("minecraft:iron_leggings"),
        "golden" to ItemFactory.fromEncoding("minecraft:golden_leggings"),
        "diamond" to ItemFactory.fromEncoding("minecraft:diamond_leggings")
    )),
    BOOTS("Boots", ItemFactory.fromEncoding("minecraft:diamond_boots"), mapOf(
        "leather" to ItemFactory.fromEncoding("minecraft:leather_boots"),
        "chain" to ItemFactory.fromEncoding("minecraft:chainmail_boots"),
        "iron" to ItemFactory.fromEncoding("minecraft:iron_boots"),
        "golden" to ItemFactory.fromEncoding("minecraft:golden_boots"),
        "diamond" to ItemFactory.fromEncoding("minecraft:diamond_boots")
    )),
    RING("Ring", ItemFactory.fromEncoding("minecraft:flint_and_steel#2"), mapOf(
        "ring_base_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#1"),
        "ring_base_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#2"),
        "ring_earth_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#3"),
        "ring_earth_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#4"),
        "ring_thunder_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#5"),
        "ring_thunder_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#6"),
        "ring_fire_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#7"),
        "ring_fire_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#8"),
        "ring_air_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#9"),
        "ring_air_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#10"),
        "ring_water_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#11"),
        "ring_water_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#12"),
        "ring_multi_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#13"),
        "ring_multi_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#14"),
        "ring_special_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#15"),
        "ring_special_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#16"),
        "ring_special_c" to ItemFactory.fromEncoding("minecraft:flint_and_steel#17")
    )),
    NECKLACE("Necklace", ItemFactory.fromEncoding("minecraft:flint_and_steel#19"), mapOf(
        "necklace_base_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#18"),
        "necklace_base_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#19"),
        "necklace_earth_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#20"),
        "necklace_earth_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#21"),
        "necklace_thunder_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#22"),
        "necklace_thunder_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#23"),
        "necklace_fire_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#24"),
        "necklace_fire_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#25"),
        "necklace_air_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#26"),
        "necklace_air_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#27"),
        "necklace_water_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#28"),
        "necklace_water_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#29"),
        "necklace_multi_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#30"),
        "necklace_multi_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#31"),
        "necklace_special_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#32"),
        "necklace_special_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#33"),
        "necklace_special_c" to ItemFactory.fromEncoding("minecraft:flint_and_steel#34")
    )),
    BRACELET("Bracelet", ItemFactory.fromEncoding("minecraft:flint_and_steel#36"), mapOf(
        "bracelet_base_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#35"),
        "bracelet_base_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#36"),
        "bracelet_earth_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#37"),
        "bracelet_earth_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#38"),
        "bracelet_thunder_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#39"),
        "bracelet_thunder_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#40"),
        "bracelet_fire_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#41"),
        "bracelet_fire_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#42"),
        "bracelet_air_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#43"),
        "bracelet_air_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#44"),
        "bracelet_water_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#45"),
        "bracelet_water_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#46"),
        "bracelet_multi_a" to ItemFactory.fromEncoding("minecraft:flint_and_steel#47"),
        "bracelet_multi_b" to ItemFactory.fromEncoding("minecraft:flint_and_steel#48")
    )),
    INVALID("???", ERROR_ITEM, emptyMap());

    companion object {
        private val VALUE_MAP: MutableMap<String, EquipmentType> = LinkedHashMap()
        init {
            values().forEach { VALUE_MAP[it.name.lowercase(Locale.getDefault())] = it }
        }

        fun getEquipmentType(id: String): EquipmentType {
            return VALUE_MAP.getOrDefault(id.lowercase(Locale.getDefault()), INVALID)
        }
    }

    override fun getKey(): String = name

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.item_type.${getKey().lowercase(Locale.getDefault())}"
    }

    fun getTexture(key: String): ItemStack {
        val texture: ItemStack? = textureMap[key.lowercase(Locale.getDefault())]
        return (texture ?: getIcon()).copy()
    }

    fun getIcon(): ItemStack {
        return icon
    }
}
