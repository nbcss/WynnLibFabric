package io.github.nbcss.wynnlib.matcher.item

import io.github.nbcss.wynnlib.items.Powder
import io.github.nbcss.wynnlib.registry.PowderRegistry
import net.minecraft.item.ItemStack
import net.minecraft.text.Text


object PowderMatcher : ItemMatcher {
    private final val powderNamePattern = Regex.fromLiteral("Powder (?:I|II|III|IV|V|VI){1}$")

    override fun toItem(item: ItemStack, name: String, tooltip: List<Text>): Powder? {
        return if (name.contains(powderNamePattern)) {
            PowderRegistry.fromName(name.substring(2))
        } else null
    }
}