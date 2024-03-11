package net.errorcraft.itematic.mixin.scoreboard;

import net.errorcraft.itematic.scoreboard.ScoreboardCriterionUtil;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.ScoreboardState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScoreboardState.class)
public class ScoreboardStateExtender {
    @Inject(
        method = "readObjectivesNbt",
        at = @At("HEAD")
    )
    private void setLookup(NbtList nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo info) {
        ScoreboardCriterionUtil.setLookup(registries);
    }

    @Inject(
        method = "readObjectivesNbt",
        at = @At("TAIL")
    )
    private void resetLookup(NbtList nbt, RegistryWrapper.WrapperLookup registries, CallbackInfo info) {
        ScoreboardCriterionUtil.setLookup(null);
    }
}
