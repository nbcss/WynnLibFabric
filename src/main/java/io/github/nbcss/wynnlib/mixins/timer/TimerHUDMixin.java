package io.github.nbcss.wynnlib.mixins.timer;

import io.github.nbcss.wynnlib.timer.IconIndicator;
import io.github.nbcss.wynnlib.timer.SideIndicator;
import io.github.nbcss.wynnlib.timer.TimerManager;
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

import java.util.List;

import static io.github.nbcss.wynnlib.utils.UtilsKt.formatTimer;

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
        List<SideIndicator> timers = TimerManager.INSTANCE.getSideTimers();
        int posX = 3;
        int posY = (client.getWindow().getScaledHeight() - 11 * timers.size()) / 2;
        for (SideIndicator timer : timers) {
            timer.render(matrices, getTextRenderer(), posX, posY);
            posY += 11;
        }
    }

    private void renderIconTimers(MatrixStack matrices, float delta) {
        List<IconIndicator> timers = TimerManager.INSTANCE.getIconTimers();
        //unit = 28 per timer
        int posX = client.getWindow().getScaledWidth() / 2 - timers.size() * 14;
        int posY = client.getWindow().getScaledHeight() - 108;
        for (IconIndicator timer : timers) {
            timer.render(matrices, getTextRenderer(), posX, posY, delta);
            /*RenderKit.INSTANCE.renderTexture(matrices, background, posX + 3, posY,
                    0, 256 - 22, 22, 22);
            Double duration = timer.getDuration();
            Double maxDuration = timer.getFullDuration();
            if(duration != null && maxDuration != null) {
                double pct = MathHelper.clamp(duration / maxDuration, 0.0, 1.0);
                Color color = Color.Companion.getAQUA();
                if (!timer.isCooldown()) {
                    color = new Color(MathHelper.hsvToRgb((float) (pct / 3.0), 1.0F, 1.0F));
                }
                int[] uv = pctToUv(timer.isCooldown() ? 1 - pct : pct);
                RenderKit.INSTANCE.renderTextureWithColor(matrices, background, color.toSolidColor(),
                        posX + 3, posY, uv[0], uv[1], 22, 22, 256, 256);
                String time = Math.round(duration) + "s";
                if (duration < 9.95) {
                    time = String.format("%.1fs", duration);
                }
                int textX = posX + 14 - getTextRenderer().getWidth(time) / 2;
                int textY = posY + 25;
                Text text = new LiteralText(time).formatted(Formatting.WHITE);
                RenderKit.INSTANCE.renderDefaultOutlineText(matrices, text, textX, textY);
            }
            RenderKit.INSTANCE.renderTexture(matrices, timer.getIcon(), posX + 5, posY + 2,
                    0, 0, 18, 18, 18, 18);*/
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
