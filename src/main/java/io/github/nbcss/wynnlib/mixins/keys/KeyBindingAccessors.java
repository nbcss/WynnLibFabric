package io.github.nbcss.wynnlib.mixins.keys;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(KeyBinding.class)
public interface KeyBindingAccessors {
    @Accessor
    static Map<InputUtil.Key, KeyBinding> getKEY_TO_BINDINGS(){
        throw new AssertionError();
    }

    @Accessor
    static Map<String, KeyBinding> getKEYS_BY_ID(){
        throw new AssertionError();
    }
}
