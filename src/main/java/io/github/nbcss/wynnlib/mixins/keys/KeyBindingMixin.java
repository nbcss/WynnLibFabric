package io.github.nbcss.wynnlib.mixins.keys;

import io.github.nbcss.wynnlib.utils.keys.UnregisteredKeyBinding;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Map;

@Mixin(KeyBinding.class)
public class KeyBindingMixin {
    @Final
    @Shadow
    private static Map<InputUtil.Key, KeyBinding> KEY_TO_BINDINGS;
    @Redirect(method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    public Object init(Map<Object, Object> instance, Object key, Object value){
        if (instance == (Object) KEY_TO_BINDINGS && value instanceof UnregisteredKeyBinding) {
            return null;
        }
        return instance.put(key, value);
    }

    @Redirect(method = "updateKeysByCode", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"))
    private static Object update(Map<Object, Object> instance, Object key, Object value){
        if (instance == (Object) KEY_TO_BINDINGS && value instanceof UnregisteredKeyBinding) {
            return null;
        }
        return instance.put(key, value);
    }
}
