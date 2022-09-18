package io.github.nbcss.wynnlib.matcher

import com.google.gson.JsonObject
import io.github.nbcss.wynnlib.Settings
import io.github.nbcss.wynnlib.utils.Color
import io.github.nbcss.wynnlib.utils.JsonGetter

abstract class AbstractMatcherType(private val color: Color): MatcherType {
    private var customColor: Color = color

    override fun reload(data: JsonObject) {
        customColor = Color(JsonGetter.getOr(data, "color", color.code()))
    }

    override fun getData(): JsonObject {
        val data = JsonObject()
        data.addProperty("color", customColor.code())
        return data
    }

    override fun getColor(): Color {
        return customColor
    }

    override fun setColor(color: Color) {
        this.customColor = color
        Settings.markDirty()
    }

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.matcher_type." + getKey().lowercase()
    }
}