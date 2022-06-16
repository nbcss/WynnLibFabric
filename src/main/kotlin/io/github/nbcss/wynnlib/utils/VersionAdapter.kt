package io.github.nbcss.wynnlib.utils

import net.minecraft.text.TranslatableText


fun translate(key: String, vararg args: Any): TranslatableText {
    return TranslatableText(key, *args)
}