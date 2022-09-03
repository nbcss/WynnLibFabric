package io.github.nbcss.wynnlib.analysis.properties.equipment

import io.github.nbcss.wynnlib.analysis.properties.AnalysisProperty
import io.github.nbcss.wynnlib.data.PowderSpecial
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.tierOf
import net.minecraft.text.Text
import java.util.regex.Pattern

class PowderSpecialProperty: AnalysisProperty {
    companion object {
        private val SPEC_NAME_PATTERN = Pattern.compile("\u00C0\u00C0[✤✦❉✹❋] (.+)")
        private val DURATION_PATTERN = Pattern.compile("Duration: (\\d+\\.?\\d*)")
        private val DAMAGE_PATTERN = Pattern.compile("Damage: (\\+?\\d+\\.?\\d*)")
        private val BOOST_PATTERN = Pattern.compile("Bonus Damage: \\+(\\d+\\.?\\d*)")
        private val RADIUS_PATTERN = Pattern.compile("Radius: (\\d+\\.?\\d*)")
        private val CHAINS_PATTERN = Pattern.compile("Chains: (\\d+\\.?\\d*)")
        private val HEALTH_LOST_PATTERN = Pattern.compile("Min\\. Lost Health: (\\d+\\.?\\d*)")
        //private val KNOCKBACK_PATTERN = Pattern.compile("Knockback: (\\d+\\.?\\d*)")
        private val FACTORY_MAP: Map<String, SpecFactory> = mapOf(
            pairs = (1..5).map { listOf(
                QuakeSpec(it),
                RageSpec(it),
                ChainLightningSpec(it),
                KillStreakSpec(it),
                CourageSpec(it),
                EnduranceSpec(it),
                CurseSpec(it),
                ConcentrationSpec(it),
                WindPrisonSpec(it),
                DodgeSpec(it),
            ) }.flatten().map { it.getKey() to it }.toTypedArray()
        )

        private fun toDataString(text: Text): String? {
            if (text.siblings.isEmpty())
                return null
            val base = text.siblings[0]
            if (base.siblings.size < 2)
                return null
            return base.siblings[1].asString()
        }

        private fun readValue(text: Text, pattern: Pattern): Double {
            toDataString(text)?.let {
                val matcher = pattern.matcher(it)
                if(matcher.find()) {
                    return matcher.group(1).toDouble()
                }
            }
            return 0.0
        }
        const val KEY = "POWDER_SPEC"
    }
    private var powderSpec: PowderSpecial? = null

    fun getPowderSpecial(): PowderSpecial? = powderSpec

    override fun set(tooltip: List<Text>, line: Int): Int {
        if (tooltip[line].siblings.isEmpty())
            return 0
        val base = tooltip[line].siblings[0]
        val matcher = SPEC_NAME_PATTERN.matcher(base.asString())
        if(matcher.find()){
            FACTORY_MAP[matcher.group(1)]?.let { factory ->
                val result = factory.read(tooltip, line + 1)
                powderSpec = result.first
                return 1 + result.second
            }
        }
        return 0
    }

    override fun getKey(): String = KEY

    abstract class SpecFactory(val tier: Int): Keyed {
        abstract val name: String
        abstract fun read(tooltip: List<Text>, line: Int): Pair<PowderSpecial?, Int>
        override fun getKey(): String = name.replace("$", tierOf(tier))
    }

    class QuakeSpec(tier: Int) : SpecFactory(tier) {
        override val name: String = "Quake $"
        override fun read(tooltip: List<Text>, line: Int): Pair<PowderSpecial?, Int> {
            if (line + 2 > tooltip.size)
                return null to 0
            val damage = readValue(tooltip[line], DAMAGE_PATTERN)
            val radius = readValue(tooltip[line + 1], RADIUS_PATTERN)
            val spec = PowderSpecial.Quake(radius, damage.toInt(), tier)
            return spec to 2
        }
    }

    class RageSpec(tier: Int) : SpecFactory(tier) {
        override val name: String = "Rage $ [Health Missing]"
        override fun read(tooltip: List<Text>, line: Int): Pair<PowderSpecial?, Int> {
            if (line + 1 > tooltip.size)
                return null to 0
            val damage = readValue(tooltip[line], DAMAGE_PATTERN)
            val healthLost = readValue(tooltip[line + 1], HEALTH_LOST_PATTERN)
            val spec = PowderSpecial.Rage(damage, healthLost, tier)
            return spec to 2
        }
    }

    class ChainLightningSpec(tier: Int) : SpecFactory(tier) {
        override val name: String = "Chain Lightning $"
        override fun read(tooltip: List<Text>, line: Int): Pair<PowderSpecial?, Int> {
            if (line + 2 > tooltip.size)
                return null to 0
            val damage = readValue(tooltip[line], DAMAGE_PATTERN)
            val chains = readValue(tooltip[line + 1], CHAINS_PATTERN)
            val spec = PowderSpecial.ChainLightning(chains.toInt(), damage.toInt(), tier)
            return spec to 2
        }
    }

    class KillStreakSpec(tier: Int) : SpecFactory(tier) {
        override val name: String = "Kill Streak $ [Mob Killed]"
        override fun read(tooltip: List<Text>, line: Int): Pair<PowderSpecial?, Int> {
            if (line + 2 > tooltip.size)
                return null to 0
            val damage = readValue(tooltip[line], BOOST_PATTERN)
            val duration = readValue(tooltip[line + 1], DURATION_PATTERN)
            val spec = PowderSpecial.KillStreak(damage, duration, tier)
            return spec to 2
        }
    }

    class CourageSpec(tier: Int) : SpecFactory(tier) {
        override val name: String = "Courage $"
        override fun read(tooltip: List<Text>, line: Int): Pair<PowderSpecial?, Int> {
            if (line + 3 > tooltip.size)
                return null to 0
            val damage = readValue(tooltip[line], DAMAGE_PATTERN)
            val boost = readValue(tooltip[line + 1], BOOST_PATTERN)
            val duration = readValue(tooltip[line + 2], DURATION_PATTERN)
            val spec = PowderSpecial.Courage(duration, damage, boost, tier)
            return spec to 3
        }
    }

    class EnduranceSpec(tier: Int) : SpecFactory(tier) {
        override val name: String = "Endurance $ [Hit Taken]"
        override fun read(tooltip: List<Text>, line: Int): Pair<PowderSpecial?, Int> {
            if (line + 2 > tooltip.size)
                return null to 0
            val damage = readValue(tooltip[line], BOOST_PATTERN)
            val duration = readValue(tooltip[line + 1], DURATION_PATTERN)
            val spec = PowderSpecial.Endurance(damage, duration, tier)
            return spec to 2
        }
    }

    class CurseSpec(tier: Int) : SpecFactory(tier) {
        override val name: String = "Curse $"
        override fun read(tooltip: List<Text>, line: Int): Pair<PowderSpecial?, Int> {
            if (line + 2 > tooltip.size)
                return null to 0
            val boost = readValue(tooltip[line], BOOST_PATTERN)
            val radius = readValue(tooltip[line + 1], RADIUS_PATTERN)
            val duration = readValue(tooltip[line + 2], DURATION_PATTERN)
            val spec = PowderSpecial.Curse(duration, boost, radius, tier)
            return spec to 3
        }
    }

    class ConcentrationSpec(tier: Int) : SpecFactory(tier) {
        override val name: String = "Concentration $ [Mana Used]"
        override fun read(tooltip: List<Text>, line: Int): Pair<PowderSpecial?, Int> {
            if (line + 2 > tooltip.size)
                return null to 0
            val damage = readValue(tooltip[line], BOOST_PATTERN)
            val duration = readValue(tooltip[line + 1], DURATION_PATTERN)
            val spec = PowderSpecial.Concentration(damage, duration, tier)
            return spec to 2
        }
    }

    class WindPrisonSpec(tier: Int) : SpecFactory(tier) {
        override val name: String = "Wind Prison $"
        override fun read(tooltip: List<Text>, line: Int): Pair<PowderSpecial?, Int> {
            if (line + 3 > tooltip.size)
                return null to 0
            val boost = readValue(tooltip[line], BOOST_PATTERN)
            val duration = readValue(tooltip[line + 1], DURATION_PATTERN)
            val spec = PowderSpecial.WindPrison(duration, boost, tier)
            return spec to 2
        }
    }

    class DodgeSpec(tier: Int) : SpecFactory(tier) {
        override val name: String = "Dodge $ [Near Mobs]"
        override fun read(tooltip: List<Text>, line: Int): Pair<PowderSpecial?, Int> {
            if (line + 2 > tooltip.size)
                return null to 0
            val damage = readValue(tooltip[line], BOOST_PATTERN)
            val duration = readValue(tooltip[line + 1], DURATION_PATTERN)
            val spec = PowderSpecial.Dodge(damage, duration, tier)
            return spec to 2
        }
    }
}