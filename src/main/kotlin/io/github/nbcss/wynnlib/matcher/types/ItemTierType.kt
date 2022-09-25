package io.github.nbcss.wynnlib.matcher.types

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.data.Tier
import io.github.nbcss.wynnlib.matcher.AbstractMatcherType
import io.github.nbcss.wynnlib.matcher.ProtectableType
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.JsonGetter
import net.minecraft.text.Text

class ItemTierType(val tier: Tier): AbstractMatcherType(Color.fromFormatting(tier.formatting)), ProtectableType {
    companion object {
        fun keyOf(tier: Tier): String = "ITEM_TIER_" + tier.name
    }
    private var protected: Boolean = tier == Tier.MYTHIC

    override fun getKey(): String {
        return keyOf(tier)
    }

    override fun reload(data: JsonObject) {
        super.reload(data)
        this.protected = JsonGetter.getOr(data, "protected", tier == Tier.MYTHIC)
    }

    override fun getData(): JsonObject {
        val data = super.getData()
        data.addProperty("protected", protected)
        return data
    }

    override fun getDisplayText(): Text {
        return formatted(tier.formatting)
    }

    override fun isProtected(): Boolean = protected

    override fun setProtected(value: Boolean) {
        this.protected = value
        Settings.markDirty()
    }
}