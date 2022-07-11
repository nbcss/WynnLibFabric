package io.github.nbcss.wynnlib.abilities.builder.entries

import io.github.nbcss.wynnlib.abilities.Ability
import io.github.nbcss.wynnlib.abilities.PropertyProvider
import io.github.nbcss.wynnlib.abilities.builder.EntryContainer
import io.github.nbcss.wynnlib.abilities.properties.AbilityProperty
import io.github.nbcss.wynnlib.abilities.properties.info.UpgradeProperty
import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.i18n.Translations
import io.github.nbcss.wynnlib.i18n.Translations.TOOLTIP_SHIFT_UPGRADE
import io.github.nbcss.wynnlib.utils.Keyed
import io.github.nbcss.wynnlib.utils.KeysKit
import io.github.nbcss.wynnlib.utils.formattingLines
import io.github.nbcss.wynnlib.utils.replaceProperty
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier

abstract class PropertyEntry(private val ability: Ability,
                             private val icon: Identifier,
                             private val upgradable: Boolean): Keyed, PropertyProvider {
    companion object {
        private val factoryMap: Map<String, Factory> = mapOf(
            pairs = listOf(
                SpellEntry,
                ReplaceSpellEntry,
                ExtendEntry,
                BasicEntry
            ).map { it.getKey().uppercase() to it }.toTypedArray()
        )

        fun getFactory(id: String?): Factory {
            return if (id != null) (factoryMap[id.uppercase()] ?: BasicEntry) else BasicEntry
        }
    }
    protected val properties: MutableMap<String, AbilityProperty> = LinkedHashMap()
    private val placeholderMap: MutableMap<String, String> = HashMap()
    private val upgrades: MutableList<Ability> = ArrayList()

    override fun getProperty(key: String): AbilityProperty? = properties[key]

    fun getAbility(): Ability = ability

    fun clearProperty(key: String) {
        properties.remove(key)
    }

    fun setProperty(key: String, property: AbilityProperty) {
        properties[key] = property
    }

    fun addUpgrade(ability: Ability) {
        if (ability != this.ability){
            upgrades.add(ability)
        }
    }

    fun getUpgrades(): List<Ability> {
        return upgrades
    }

    open fun getDisplayNameText(): MutableText {
        return getAbility().translate().formatted(getAbility().getTier().getFormatting())
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

    fun getAbilityDescriptionTooltip(ability: Ability): List<Text> {
        //fixme replace with own placeholder
        val desc = replaceProperty(replaceProperty(ability.translate("desc").string, '$')
        { ability.getPlaceholder(it) }, '@') {
            val name = if (it.startsWith(".")) "wynnlib.ability.name${it.lowercase()}" else it
            Translatable.from(name).translate().string
        }
        return formattingLines(desc, Formatting.GRAY.toString()).toList()
    }

    fun getUpgradeTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        if (upgradable && upgrades.isNotEmpty()){
            tooltip.add(Translations.TOOLTIP_ABILITY_UPGRADE.formatted(Formatting.GRAY).append(":"))
            for (upgrade in upgrades) {
                if (KeysKit.isShiftDown()){
                    tooltip.add(LiteralText("+ ").formatted(Formatting.AQUA)
                        .append(upgrade.formatted(upgrade.getTier().getFormatting())))
                    for (text in getAbilityDescriptionTooltip(upgrade)) {
                        tooltip.add(LiteralText("- ").formatted(Formatting.BLACK).append(text))
                    }
                }else{
                    tooltip.add(LiteralText("- ").formatted(Formatting.DARK_GRAY)
                        .append(upgrade.formatted(upgrade.getTier().getFormatting())))
                }
            }
            if(!KeysKit.isShiftDown()){
                tooltip.add(LiteralText.EMPTY)
                tooltip.add(TOOLTIP_SHIFT_UPGRADE.formatted(Formatting.GREEN))
            }
        }
        return tooltip
    }

    open fun getTooltip(): List<Text> {
        val tooltip: MutableList<Text> = ArrayList()
        tooltip.add(getDisplayNameText().append(" ${getTierText()}").formatted(Formatting.BOLD))
        tooltip.add(LiteralText.EMPTY)
        tooltip.addAll(getAbilityDescriptionTooltip(ability))
        //Add effect tooltip
        val propertyTooltip = getPropertiesTooltip()
        if (propertyTooltip.isNotEmpty()){
            tooltip.add(LiteralText.EMPTY)
            tooltip.addAll(propertyTooltip)
        }
        val upgradeTooltip = getUpgradeTooltip()
        if (upgradeTooltip.isNotEmpty()){
            tooltip.add(LiteralText.EMPTY)
            tooltip.addAll(upgradeTooltip)
        }
        return tooltip
    }

    override fun getKey(): String = ability.getKey()

    open fun getSideText(): Text = LiteralText.EMPTY

    fun getTexture(): Identifier = icon

    fun getTier(): Int {
        return 1 + getUpgrades().size
    }

    fun getTierText(): String {
        return if (upgradable){
            when (getTier()){
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
        }else{
            ""
        }
    }

    interface Factory: Keyed {
        fun create(container: EntryContainer,
                   ability: Ability,
                   texture: Identifier,
                   upgradable: Boolean): PropertyEntry?
    }
}