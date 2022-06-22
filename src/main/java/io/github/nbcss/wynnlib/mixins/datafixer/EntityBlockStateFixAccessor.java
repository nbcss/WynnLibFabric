package io.github.nbcss.wynnlib.mixins.datafixer;

import net.minecraft.datafixer.fix.EntityBlockStateFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;


import java.util.Map;

@Mixin(EntityBlockStateFix.class)
public interface EntityBlockStateFixAccessor {
    @Accessor("BLOCK_NAME_TO_ID")
    static Map<String, Integer> getBLOCK_NAME_TO_ID() {
        throw new AssertionError();
    }
}
