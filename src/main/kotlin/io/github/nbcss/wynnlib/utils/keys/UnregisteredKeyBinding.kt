package io.github.nbcss.wynnlib.utils.keys

import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil

class UnregisteredKeyBinding(translationKey: String,
                             category: String,
                             code: Int): KeyBinding(translationKey, InputUtil.Type.KEYSYM, code, category)