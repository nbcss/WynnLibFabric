package io.github.nbcss.wynnlib.data

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translations.SUFFIX_POWDER_SPEC_BLOCKS
import io.github.nbcss.wynnlib.i18n.Translations.SUFFIX_POWDER_SPEC_DAM_PER_MANA
import io.github.nbcss.wynnlib.i18n.Translations.SUFFIX_POWDER_SPEC_SEC
import io.github.nbcss.wynnlib.i18n.Translations.SUFFIX_POWDER_SPEC_SEC_PER_MANA
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_CHAINS
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_DAMAGE
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_DAMAGE_BOOST
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_DURATION
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_KNOCKBACK
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_POWDER_SPEC_RADIUS
import io.github.nbcss.wynnlib.registry.Registry
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.removeDecimal
import io.github.nbcss.wynnlib.utils.signed
import io.github.nbcss.wynnlib.utils.tierOf
import net.minecraft.text.LiteralText
import net.minecraft.text.Text
import net.minecraft.util.Formatting

abstract class PowderSpecial(private val tier: Int): Keyed {
    companion object: Registry<PowderSpecial>() {
        private const val RESOURCE = "assets/wynnlib/data/PowderSpecs.json"
        private val PROPERTY_MAP: MutableMap<String, PowderSpecial> = mutableMapOf()
        private val factoryMap: Map<String, Factory<out PowderSpecial>> = mapOf(
            pairs = listOf(
                Quake, Rage,                //earth
                ChainLightning, KillStreak,  //thunder
                Courage, Endurance,         //fire
                Curse, Concentration,       //water
                WindPrison, Dodge,          //air
            ).map { it.type.name.uppercase() to it }.toTypedArray()
        )

        override fun getFilename(): String = RESOURCE

        override fun reload(array: JsonArray) {
            PROPERTY_MAP.clear()
            super.reload(array)
        }

        override fun put(item: PowderSpecial) {
            PROPERTY_MAP[item.getPropertyKey()] = item
            super.put(item)
        }

        override fun read(data: JsonObject): PowderSpecial? {
            return factoryMap[data["type"].asString.uppercase()]?.fromData(data)
        }

        fun fromPropertyKey(key: String): PowderSpecial? = PROPERTY_MAP[key]
    }
    fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = mutableListOf()
        val color = getType().getElement().color
        val title = getType().formatted(color)
        if (getTier() > 0) {
            title.append(LiteralText(" [${tierOf(getTier())}]").formatted(Formatting.DARK_GRAY))
        }
        tooltip.add(title)
        for (text in getPropertyTooltip()) {
            tooltip.add(LiteralText("- ").formatted(color).append(text))
        }
        return tooltip
    }
    abstract fun getType(): Type
    abstract fun getPropertyTooltip(): List<Text>
    abstract fun getPropertyKey(): String

    fun getTier(): Int = tier
    override fun getKey(): String {
        return getType().name.lowercase() + ":" + getTier()
    }

    class Quake(private val radius: Double,
                private val damage: Int,
                tier: Int) : PowderSpecial(tier) {
        companion object: Factory<Quake> {
            override val type: Type = Type.QUAKE
            override fun fromData(data: JsonObject): Quake {
                val radius = data["radius"].asDouble
                val damage = data["damage"].asInt
                val tier = data["tier"].asInt
                return Quake(radius, damage, tier)
            }
        }

        override fun getType(): Type = Type.QUAKE

        override fun getPropertyTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            tooltip.add((LiteralText("${TOOLTIP_POWDER_SPEC_RADIUS.translate().string}: ")
                .formatted(Formatting.GRAY))
                .append(SUFFIX_POWDER_SPEC_BLOCKS.formatted(Formatting.GRAY, label = null, radius)))
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: ${damage}% ✤")
                .formatted(Formatting.GRAY))
            return tooltip
        }

        override fun getPropertyKey(): String {
            return "QUAKE{$radius/$damage}"
        }
    }

    class Rage(private val damage: Double,
               tier: Int) : PowderSpecial(tier) {
        companion object: Factory<Rage> {
            override val type: Type = Type.RAGE
            override fun fromData(data: JsonObject): Rage {
                val damage = data["damage"].asDouble
                val tier = data["tier"].asInt
                return Rage(damage, tier)
            }
        }

        override fun getType(): Type = Type.RAGE

        override fun getPropertyTooltip(): List<Text> {
            return listOf(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: " +
                    "+${removeDecimal(damage)}% ✤").formatted(Formatting.GRAY))
        }

        override fun getPropertyKey(): String {
            return "RAGE{$damage}"
        }
    }

    class ChainLightning(private val chains: Int,
                         private val damage: Int,
                         tier: Int) : PowderSpecial(tier) {
        companion object: Factory<ChainLightning> {
            override val type: Type = Type.CHAIN_LIGHTNING
            override fun fromData(data: JsonObject): ChainLightning {
                val chains = data["chains"].asInt
                val damage = data["damage"].asInt
                val tier = data["tier"].asInt
                return ChainLightning(chains, damage, tier)
            }
        }

        override fun getType(): Type = Type.CHAIN_LIGHTNING

        override fun getPropertyTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_CHAINS.translate().string}: $chains")
                    .formatted(Formatting.GRAY))
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: ${damage}% ✦")
                    .formatted(Formatting.GRAY))
            return tooltip
        }

        override fun getPropertyKey(): String {
            //Chain Lightning damage is wrong in game, so have to ignore it in match key
            return "CHAIN_LIGHTNING{$chains}"
        }
    }

    class KillStreak(private val damage: Double,
                     private val duration: Double,
                     tier: Int) : PowderSpecial(tier) {
        companion object: Factory<KillStreak> {
            override val type: Type = Type.KILL_STREAK
            override fun fromData(data: JsonObject): KillStreak {
                val damage = data["damage"].asDouble
                val duration = data["duration"].asDouble
                val tier = data["tier"].asInt
                return KillStreak(damage, duration, tier)
            }
        }

        override fun getType(): Type = Type.KILL_STREAK

        override fun getPropertyTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: " +
                        "+${removeDecimal(damage)}% ✦").formatted(Formatting.GRAY))
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                .formatted(Formatting.GRAY)
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
            return tooltip
        }

        override fun getPropertyKey(): String {
            return "KILL_STREAK{$damage/$duration}"
        }
    }

    class Courage(private val duration: Double,
                  private val damage: Double,
                  private val boost: Double,
                  tier: Int) : PowderSpecial(tier) {
        companion object: Factory<Courage> {
            override val type: Type = Type.COURAGE
            override fun fromData(data: JsonObject): Courage {
                val duration = data["duration"].asDouble
                val damage = data["damage"].asDouble
                val boost = data["boost"].asDouble
                val tier = data["tier"].asInt
                return Courage(duration, damage, boost, tier)
            }
        }

        override fun getType(): Type = Type.COURAGE

        override fun getPropertyTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY)
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: ${removeDecimal(damage)}% ✹")
                    .formatted(Formatting.GRAY))
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE_BOOST.translate().string}: " +
                        "+${removeDecimal(boost)}%")
                    .formatted(Formatting.GRAY))
            return tooltip
        }

        override fun getPropertyKey(): String {
            return "COURAGE{$duration/$damage/$boost}"
        }
    }

    class Endurance(private val damage: Double,
                    private val duration: Double,
                    tier: Int) : PowderSpecial(tier) {
        companion object: Factory<Endurance> {
            override val type: Type = Type.ENDURANCE
            override fun fromData(data: JsonObject): Endurance {
                val damage = data["damage"].asDouble
                val duration = data["duration"].asDouble
                val tier = data["tier"].asInt
                return Endurance(damage, duration, tier)
            }
        }

        override fun getType(): Type = Type.ENDURANCE

        override fun getPropertyTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: " +
                        "+${removeDecimal(damage)}% ✹")
                    .formatted(Formatting.GRAY))
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY)
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
            return tooltip
        }

        override fun getPropertyKey(): String {
            return "ENDURANCE{$damage/$duration}"
        }
    }

    class Curse(private val duration: Double,
                private val boost: Double,
                tier: Int) : PowderSpecial(tier) {
        companion object: Factory<Curse> {
            override val type: Type = Type.CURSE
            override fun fromData(data: JsonObject): Curse {
                val duration = data["duration"].asDouble
                val boost = data["boost"].asDouble
                val tier = data["tier"].asInt
                return Curse(duration, boost, tier)
            }
        }

        override fun getType(): Type = Type.CURSE

        override fun getPropertyTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY)
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE_BOOST.translate().string}: " +
                        "+${removeDecimal(boost)}%")
                    .formatted(Formatting.GRAY))
            return tooltip
        }

        override fun getPropertyKey(): String {
            return "CURSE{$duration/$boost}"
        }
    }

    class Concentration(private val damage: Double,
                        private val duration: Double,
                        tier: Int) : PowderSpecial(tier) {
        companion object: Factory<Concentration> {
            override val type: Type = Type.CONCENTRATION
            override fun fromData(data: JsonObject): Concentration {
                val damage = data["damage"].asDouble
                val duration = data["duration"].asDouble
                val tier = data["tier"].asInt
                return Concentration(damage, duration, tier)
            }
        }

        override fun getType(): Type = Type.CONCENTRATION

        override fun getPropertyTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: ")
                    .formatted(Formatting.GRAY)
                .append(SUFFIX_POWDER_SPEC_DAM_PER_MANA.formatted(Formatting.GRAY, label = null,
                    "+${removeDecimal(damage)}%")))
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY)
                .append(SUFFIX_POWDER_SPEC_SEC_PER_MANA.formatted(Formatting.GRAY, label = null,
                    removeDecimal(duration))))
            return tooltip
        }

        override fun getPropertyKey(): String {
            return "CONCENTRATION{$damage/$duration}"
        }
    }

    class WindPrison(private val duration: Double,
                     private val boost: Double,
                     private val knockback: Int,
                     tier: Int) : PowderSpecial(tier) {
        companion object: Factory<WindPrison> {
            override val type: Type = Type.WIND_PRISON
            override fun fromData(data: JsonObject): WindPrison {
                val duration = data["duration"].asDouble
                val boost = data["boost"].asDouble
                val knockback = data["knockback"].asInt
                val tier = data["tier"].asInt
                return WindPrison(duration, boost, knockback, tier)
            }
        }

        override fun getType(): Type = Type.WIND_PRISON

        override fun getPropertyTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY)
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE_BOOST.translate().string}: " +
                        "+${removeDecimal(boost)}%")
                    .formatted(Formatting.GRAY))
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_KNOCKBACK.translate().string}: ")
                    .formatted(Formatting.GRAY)
                .append(SUFFIX_POWDER_SPEC_BLOCKS.formatted(Formatting.GRAY, label = null, knockback)))
            return tooltip
        }

        override fun getPropertyKey(): String {
            return "WIND_PRISON{$duration/$boost/$knockback}"
        }
    }

    class Dodge(private val damage: Double,
                private val duration: Double,
                tier: Int) : PowderSpecial(tier) {
        companion object: Factory<Dodge> {
            override val type: Type = Type.DODGE
            override fun fromData(data: JsonObject): Dodge {
                val damage = data["damage"].asDouble
                val duration = data["duration"].asDouble
                val tier = data["tier"].asInt
                return Dodge(damage, duration, tier)
            }
        }

        override fun getType(): Type = Type.DODGE

        override fun getPropertyTooltip(): List<Text> {
            val tooltip: MutableList<Text> = mutableListOf()
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DAMAGE.translate().string}: " +
                        "+${removeDecimal(damage)}% ❋")
                    .formatted(Formatting.GRAY))
            tooltip.add(LiteralText("${TOOLTIP_POWDER_SPEC_DURATION.translate().string}: ")
                    .formatted(Formatting.GRAY)
                .append(SUFFIX_POWDER_SPEC_SEC.formatted(Formatting.GRAY, label = null, removeDecimal(duration))))
            return tooltip
        }

        override fun getPropertyKey(): String {
            return "DODGE{$damage/$duration}"
        }
    }

    interface Factory<T: PowderSpecial> {
        val type: Type
        fun fromData(data: JsonObject): T
    }

    enum class Type(private val element: Element): Translatable {
        QUAKE(Element.EARTH),
        CHAIN_LIGHTNING(Element.THUNDER),
        CURSE(Element.WATER),
        COURAGE(Element.FIRE),
        WIND_PRISON(Element.AIR),
        RAGE(Element.EARTH),
        KILL_STREAK(Element.THUNDER),
        CONCENTRATION(Element.WATER),
        ENDURANCE(Element.FIRE),
        DODGE(Element.AIR);
        companion object {
            fun fromWeaponElement(element: Element): Type {
                return when (element){
                    Element.FIRE -> COURAGE
                    Element.WATER -> CURSE
                    Element.AIR -> WIND_PRISON
                    Element.THUNDER -> CHAIN_LIGHTNING
                    Element.EARTH -> QUAKE
                }
            }

            fun fromArmourElement(element: Element): Type {
                return when (element){
                    Element.FIRE -> ENDURANCE
                    Element.WATER -> CONCENTRATION
                    Element.AIR -> DODGE
                    Element.THUNDER -> KILL_STREAK
                    Element.EARTH -> RAGE
                }
            }
        }

        fun getElement(): Element = element

        override fun getTranslationKey(label: String?): String {
            return "wynnlib.powder_spec.${name.lowercase()}"
        }
    }
}