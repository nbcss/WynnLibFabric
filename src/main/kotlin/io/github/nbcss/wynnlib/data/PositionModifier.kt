package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable
import io.github.nbcss.wynnlib.utils.Keyed
import java.util.*

enum class PositionModifier(private val id: String): Keyed, Translatable {
    LEFT("left"),
    RIGHT("right"),
    ABOVE("above"),
    UNDER("under"),
    TOUCHING("touching"),
    NOT_TOUCHING("notTouching");

    override fun getKey(): String = id

    override fun getTranslationKey(label: String?): String {
        val key = name.lowercase(Locale.getDefault())
        if(label == "tooltip"){
            return "wynnlib.tooltip.position_modifier.$key"
        }
        return "wynnlib.position_modifier.$key"
    }
}