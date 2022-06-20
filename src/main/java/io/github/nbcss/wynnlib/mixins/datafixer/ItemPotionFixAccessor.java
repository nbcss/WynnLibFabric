package io.github.nbcss.wynnlib.mixins.datafixer;

import net.minecraft.datafixer.fix.ItemPotionFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemPotionFix.class)
public interface ItemPotionFixAccessor {

    @Accessor("ID_TO_POTIONS")
    static String[] getID_TO_POTIONS() {
        throw new AssertionError();
    }
}
