package io.github.nbcss.wynnlib.data

import io.github.nbcss.wynnlib.i18n.Translatable

enum class MouseKey: Translatable {
    LEFT, RIGHT;

    fun opposite(): MouseKey {
        return if (this == RIGHT) LEFT else RIGHT
    }

    override fun getTranslationKey(label: String?): String {
        return "wynnlib.mouse_key.${name.lowercase()}"
    }
}