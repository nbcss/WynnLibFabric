package io.github.nbcss.wynnlib.mixins.timer;

import io.github.nbcss.wynnlib.Settings;
import io.github.nbcss.wynnlib.render.RenderKit;
import io.github.nbcss.wynnlib.timer.IconTimer;
import io.github.nbcss.wynnlib.timer.SideTimer;
import io.github.nbcss.wynnlib.timer.TimerManager;
import io.github.nbcss.wynnlib.utils.AlphaColor;
import io.github.nbcss.wynnlib.utils.Color;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.nbcss.wynnlib.utils.UtilsKt.formatTimer;
import static io.github.nbcss.wynnlib.utils.UtilsKt.round;

@Mixin(InGameHud.class)
public abstract class TimerHUDMixin {
    private final Identifier background = new Identifier("wynnlib", "textures/hud/timer_background.png");
    @Final
    @Shadow
    private MinecraftClient client;

    @Shadow public abstract TextRenderer getTextRenderer();

    @Shadow @Final private ItemRenderer itemRenderer;

    @Inject(method = "render", at = @At("HEAD"))
    public void render(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (!this.client.options.hudHidden) {
            renderSideTimers(matrices);
            renderIconTimers(matrices);
        }
    }

    private void renderSideTimers(MatrixStack matrices) {
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

    private void renderIconTimers(MatrixStack matrices) {
        List<IconTimer> timers = TimerManager.INSTANCE.getIconTimers();
        //unit = 28 per timer
        int posX = client.getWindow().getScaledWidth() / 2 - timers.size() * 14;
        int posY = client.getWindow().getScaledHeight() - 108;
        for (IconTimer timer : timers) {
            RenderKit.INSTANCE.renderTexture(matrices, background, posX + 3, posY,
                    0, 256 - 22, 22, 22);
            Double duration = timer.getDuration();
            Double maxDuration = timer.getFullDuration();
            if(duration != null && maxDuration != null) {
                double pct = MathHelper.clamp(duration / maxDuration, 0.0, 1.0);
                Color color = Color.Companion.getORANGE();
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
                    0, 0, 18, 18, 18, 18);
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
