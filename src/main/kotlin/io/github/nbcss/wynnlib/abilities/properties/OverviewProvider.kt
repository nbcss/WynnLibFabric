package io.github.nbcss.wynnlib.abilities.properties

import net.minecraft.text.Text

interface OverviewProvider {
    fun getOverviewTip(): Text?
}