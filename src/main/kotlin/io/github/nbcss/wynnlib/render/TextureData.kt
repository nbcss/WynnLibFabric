package io.github.nbcss.wynnlib.render

import net.minecraft.util.Identifier

data class TextureData(val texture: Identifier,
                       val u: Int = 0,
                       val v: Int = 0,
                       val width: Int = 256,
                       val height: Int = 256)
