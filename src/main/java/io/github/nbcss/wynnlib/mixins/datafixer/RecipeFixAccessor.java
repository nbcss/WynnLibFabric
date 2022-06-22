package io.github.nbcss.wynnlib.mixins.datafixer;

import net.minecraft.datafixer.fix.RecipeFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(RecipeFix.class)
public interface RecipeFixAccessor {

    @Accessor("RECIPES")
    static Map<String, String> getRECIPES() {
        throw new AssertionError();
    }
}