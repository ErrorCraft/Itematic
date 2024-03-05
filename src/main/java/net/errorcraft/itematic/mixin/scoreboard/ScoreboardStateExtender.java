package net.errorcraft.itematic.mixin.scoreboard;

import com.llamalad7.mixinextras.sugar.Local;
import net.errorcraft.itematic.access.scoreboard.ScoreboardAccess;
import net.errorcraft.itematic.access.scoreboard.ScoreboardStateAccess;
import net.errorcraft.itematic.scoreboard.ScoreboardCriterionUtil;
import net.errorcraft.itematic.text.TextUtil;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardState;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScoreboardState.class)
public class ScoreboardStateExtender implements ScoreboardStateAccess {
    @Unique
    private RegistryWrapper.WrapperLookup lookup;

    @Redirect(
        method = "writeNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/scoreboard/Scoreboard;toNbt()Lnet/minecraft/nbt/NbtList;"
        )
    )
    private NbtList toNbtUseDynamicRegistry(Scoreboard instance, @Local(argsOnly = true) RegistryWrapper.WrapperLookup lookup) {
        return ((ScoreboardAccess) instance).itematic$toNbt(lookup);
    }

    @Redirect(
        method = "readNbt",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/scoreboard/Scoreboard;readNbt(Lnet/minecraft/nbt/NbtList;)V"
        )
    )
    private void fromNbtUseDynamicRegistry(Scoreboard instance, NbtList list) {
        ((ScoreboardAccess) instance).itematic$readNbt(list, this.lookup);
    }

    @Redirect(
        method = { "teamsToNbt", "objectivesToNbt" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;toJsonString(Lnet/minecraft/text/Text;)Ljava/lang/String;"
        )
    )
    private String toJsonStringUseDynamicRegistry(Text text) {
        return TextUtil.toJsonString(text, this.lookup);
    }

    @Redirect(
        method = { "readTeamsNbt", "readObjectivesNbt" },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/text/Text$Serialization;fromJson(Ljava/lang/String;)Lnet/minecraft/text/MutableText;"
        )
    )
    private MutableText fromJsonUseDynamicRegistry(String json) {
        return TextUtil.fromJsonString(json, this.lookup);
    }

    @Inject(
        method = "readObjectivesNbt",
        at = @At("HEAD")
    )
    private void setLookup(NbtList nbt, CallbackInfo info) {
        ScoreboardCriterionUtil.setLookup(this.lookup);
    }

    @Inject(
        method = "readObjectivesNbt",
        at = @At("RETURN")
    )
    private void resetLookup(NbtList nbt, CallbackInfo info) {
        ScoreboardCriterionUtil.setLookup(null);
    }

    @Override
    public void itematic$setLookup(RegistryWrapper.WrapperLookup lookup) {
        this.lookup = lookup;
    }
}
