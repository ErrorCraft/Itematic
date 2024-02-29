package net.errorcraft.itematic.mixin.scoreboard;

import net.errorcraft.itematic.scoreboard.ScoreboardScoreUtil;
import net.errorcraft.itematic.text.TextUtil;
import net.minecraft.scoreboard.ScoreboardScore;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScoreboardScore.class)
public abstract class ScoreboardScoreExtender {
    @Redirect(
        method = "toNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;toJsonString(Lnet/minecraft/text/Text;)Ljava/lang/String;"
        )
    )
    private String toJsonStringUseDynamicRegistry(Text text) {
        return TextUtil.toJsonString(text, ScoreboardScoreUtil.lookup());
    }

    @Redirect(
        method = "fromNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;fromJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"
        )
    )
    private static MutableText fromJsonUseDynamicRegistry(String json) {
        return TextUtil.fromJsonString(json, ScoreboardScoreUtil.lookup());
    }
}
