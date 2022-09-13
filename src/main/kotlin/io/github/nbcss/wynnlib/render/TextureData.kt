package io.github.nbcss.wynnlib.render

import net.minecraft.util.Identifier

data class TextureData(val texture: Identifier,
                       val u: Int = 0,
                       val v: Int = 0,
                       val width: Int = 256,
                       val height: Int = 256){
    constructor(path: String,
                u: Int = 0,
                v: Int = 0,
                width: Int = 256,
                height: Int = 256): this(Identifier("wynnlib", path), u, v, width, height)
}
