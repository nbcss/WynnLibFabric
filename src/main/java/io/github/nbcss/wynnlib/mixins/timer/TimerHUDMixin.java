package io.github.nbcss.wynnlib.mixins.timer;

import io.github.nbcss.wynnlib.timer.IconIndicator;
import io.github.nbcss.wynnlib.timer.SideIndicator;
import io.github.nbcss.wynnlib.timer.IndicatorManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Collections;
import java.util.List;

@Mixin(InGameHud.class)
public abstract class TimerHUDMixin {
    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow @Final private ItemRenderer itemRenderer;

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!this.client.options.hudHidden) {
            renderSideTimers(matrices);
            renderIconTimers(matrices, tickDelta);
        }
    }

    private void renderSideTimers(MatrixStack matrices) {
        List<SideIndicator> timers = IndicatorManager.INSTANCE.getSideTimers();
        Collections.sort(timers);
        int posX = 3;
        int posY = (client.getWindow().getScaledHeight() - 11 * timers.size()) / 2;
        for (SideIndicator timer : timers) {
            timer.render(matrices, getTextRenderer(), posX, posY);
            posY += 11;
        }
    }

    private void renderIconTimers(MatrixStack matrices, float delta) {
        List<IconIndicator> timers = IndicatorManager.INSTANCE.getIconTimers();
        //unit = 28 per timer
        int posX = client.getWindow().getScaledWidth() / 2 - timers.size() * 14;
        int posY = client.getWindow().getScaledHeight() - 108;
        for (IconIndicator timer : timers) {
            timer.render(matrices, getTextRenderer(), posX, posY, delta);
            posX += 28;
        }
    }

    private int[] pctToUv(double pct) {
        int index = (int) Math.round((1 - pct) * 85);
        int u = (index % 11) * 22;
        int v = (index / 11) * 22;
        return new int[]{u, v};
    }
}
