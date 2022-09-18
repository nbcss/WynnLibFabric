package io.github.nbcss.wynnlib.utils.keys

import io.github.nbcss.wynnlib.mixins.keys.BoundKeyAccessor
import io.github.nbcss.wynnlib.mixins.keys.KeyBindingAccessors
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil

class UnregisteredKeyBinding(translationKey: String,
                             category: String,
                             code: Int): KeyBinding(translationKey, InputUtil.Type.KEYSYM, code, category){
    init {
        KeyBindingAccessors.getKEYS_BY_ID().remove(translationKey)
        val boundKey = (this as BoundKeyAccessor).getboundKey()
        KeyBindingAccessors.getKEY_TO_BINDINGS().remove(boundKey)
        updateKeysByCode()
    }
}