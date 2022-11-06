package io.github.nbcss.wynnlib.mixins.inventory;

import io.github.nbcss.wynnlib.data.CharacterClass;
import io.github.nbcss.wynnlib.gui.widgets.buttons.ATreeEditButtonWidget;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(HandledScreen.class)
public class AbilityTreeEditorMixin extends Screen {
    private static final Pattern TITLE_PATTERN = Pattern.compile("(.+) Abilities");
    protected AbilityTreeEditorMixin(Text title) {
        super(title);
    }
    @Shadow
    protected int backgroundWidth;
    @Shadow
    protected int x;
    @Shadow
    protected int y;

    @Inject(method = "init", at = @At("TAIL"))
    public void init(CallbackInfo ci){
        getTitle();
        Matcher matcher = TITLE_PATTERN.matcher(getTitle().asString());
        if (matcher.find()) {
            CharacterClass character = CharacterClass.Companion.fromId(matcher.group(1));
            if (character != null) {
                int buttonX = this.x + this.backgroundWidth + 10;
                int buttonY = this.y + 20;
                ATreeEditButtonWidget button = new ATreeEditButtonWidget(this, character, buttonX, buttonY);
                addDrawableChild(button);
            }
        }
    }
}
