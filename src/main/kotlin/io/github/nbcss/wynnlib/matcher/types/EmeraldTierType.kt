package io.github.nbcss.wynnlib.matcher.types

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.items.Emerald
import io.github.nbcss.wynnlib.matcher.AbstractMatcherType
import io.github.nbcss.wynnlib.matcher.ProtectableType
import io.github.nbcss.wynnlib.utils.JsonGetter
import net.minecraft.text.Text

class EmeraldTierType(private val tier: Emerald.Tier) : AbstractMatcherType(tier.color), ProtectableType {
    companion object {
        fun keyOf(tier: Emerald.Tier): String = "EMERALD_" + tier.name
    }

    private var protected: Boolean = false

    override fun getDisplayText(): Text = Emerald(tier).getDisplayText()

    override fun getKey(): String = keyOf(tier)

    override fun reload(data: JsonObject) {
        super.reload(data)
        this.protected = JsonGetter.getOr(data, "protected", false)
    }

    override fun isProtected(): Boolean = protected

    override fun setProtected(value: Boolean) {
        protected = value
    }
}