package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.general.BoundSpellProperty
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.formattingLines
import io.github.nbcss.wynnlib.utils.replaceProperty
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

open class PropertyEntry(private val root: Ability,
                         private val icon: Identifier): Keyed {
    companion object: Factory {
        private val factoryMap: Map<String, Factory> = mapOf(
            "SPELL" to SpellEntry,
            "REPLACE" to ReplaceSpellEntry,
            "EXTEND" to ExtendEntry,
            "BOUND" to BoundEntry,
        )

        fun createEntry(type: String,
                        container: EntryContainer,
                        ability: Ability,
                        texture: Identifier): PropertyEntry? {
            return (factoryMap[type.uppercase()] ?: this).create(container, ability, texture)
        }

        override fun create(container: EntryContainer, ability: Ability, texture: Identifier): PropertyEntry {
            return PropertyEntry(ability, texture)
        }
    }
    protected val properties: MutableMap<String, AbilityProperty> = LinkedHashMap()
    private val placeholderMap: MutableMap<String, String> = HashMap()
    private val upgrades: MutableList<Ability> = ArrayList()

    fun getAbility(): Ability = root

    fun getProperty(key: String): AbilityProperty? = properties[key]

    fun setProperty(key: String, property: AbilityProperty) {
        properties[key] = property
    }

    fun addUpgrade(ability: Ability) {
        upgrades.add(ability)
    }

    fun getUpgrades(): List<Ability> {
        return upgrades
    }

    open fun getDisplayNameText(): MutableText {
        return getAbility().translate()
            .formatted(getAbility().getTier().getFormatting())
    }

    fun getPlaceholder(key: String): String {
        return placeholderMap.getOrDefault(key, key)
    }

    fun putPlaceholder(key: String, value: String) {
        placeholderMap[key] = value
    }

    fun getPropertiesTooltip(): List<Text> {
        return properties.values.map { it.getTooltip() }.flatten()
    }

    fun getDescriptionTooltip(): List<Text> {
        //fixme replace with own placeholder
        val desc = replaceProperty(replaceProperty(root.translate("desc").string, '$')
        { root.getPlaceholder(it) }, '@') {
            val name = if (it.startsWith(".")) "wynnlib.ability.name${it.lowercase()}" else it
            Translatable.from(name).translate().string
        }
        return formattingLines(desc, 190, Formatting.GRAY.toString()).toList()
    }

    open fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(getDisplayNameText().append(" ${getTierText()}").formatted(Formatting.BOLD))
        tooltip.add(LiteralText.EMPTY)
        tooltip.addAll(getDescriptionTooltip())
        //Add effect tooltip
        val propertyTooltip = getPropertiesTooltip()
        if (propertyTooltip.isNotEmpty()){
            tooltip.add(LiteralText.EMPTY)
            tooltip.addAll(propertyTooltip)
        }
        if (upgrades.isNotEmpty()){
            tooltip.add(LiteralText.EMPTY)
            tooltip.add(LiteralText("Upgrades:").formatted(Formatting.GRAY))
            for (upgrade in upgrades) {
                tooltip.add(LiteralText("- ").formatted(Formatting.GRAY)
                    .append(upgrade.formatted(upgrade.getTier().getFormatting())))
            }
        }
        return tooltip
    }

    override fun getKey(): String = root.getKey()

    open fun getSideText(): Text = LiteralText.EMPTY

    fun getTexture(): Identifier = icon

    fun getTier(): Int {
        return 1 + getUpgrades().size
    }

    fun getTierText(): String {
        return when (getTier()){
            1 -> "I"
            2 -> "II"
            3 -> "III"
            4 -> "IV"
            5 -> "V"
            6 -> "VI"
            7 -> "VII"
            8 -> "VIII"
            9 -> "IX"
            10 -> "X"
            else -> "✰"
        }
    }

    interface Factory {
        fun create(container: EntryContainer, ability: Ability, texture: Identifier): PropertyEntry?
    }
}