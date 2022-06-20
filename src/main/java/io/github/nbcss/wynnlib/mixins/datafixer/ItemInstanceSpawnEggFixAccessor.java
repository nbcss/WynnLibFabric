package io.github.nbcss.wynnlib.mixins.datafixer;

import net.minecraft.datafixer.fix.ItemInstanceSpawnEggFix;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(ItemInstanceSpawnEggFix.class)
public interface ItemInstanceSpawnEggFixAccessor {

    @Accessor("ENTITY_SPAWN_EGGS")
    static Map<String, String> getEntitySpawnEggs(){
        throw new AssertionError();
    }
}