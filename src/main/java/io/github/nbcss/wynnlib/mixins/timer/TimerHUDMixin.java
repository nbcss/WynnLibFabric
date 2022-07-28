package io.github.nbcss.wynnlib.mixins.timer;

import io.github.nbcss.wynnlib.render.RenderKit;
import io.github.nbcss.wynnlib.timer.SideTimer;
import io.github.nbcss.wynnlib.timer.TimerManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.nbcss.wynnlib.utils.UtilsKt.formatTimer;

@Mixin(InGameHud.class)
public class TimerHUDMixin {
    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!this.client.options.hudHidden) {
            List<SideTimer> timers = TimerManager.INSTANCE.getSideTimers();
            int posX = 3;
            int posY = (client.getWindow().getScaledHeight() - 11 * timers.size()) / 2;
            for (SideTimer timer : timers) {
                LiteralText text = new LiteralText("");
                Double duration = timer.getDuration();
                if (duration != null){
                    Formatting color = Formatting.GREEN;
                    if (duration < 10) {
                        color = Formatting.RED;
                    }else if(duration < 30) {
                        color = Formatting.GOLD;
                    }
                    text.append(new LiteralText(formatTimer((long) (duration * 1000))).formatted(color))
                            .append(" ");
                }
                text.append(timer.getDisplayText());
                RenderKit.INSTANCE.renderDefaultOutlineText(matrices, text, (float) posX, (float) posY);
                posY += 11;
            }
        }
    }
}
