package net.errorcraft.itematic.mixin.scoreboard;

import net.errorcraft.itematic.access.scoreboard.ScoreboardAccess;
import net.errorcraft.itematic.scoreboard.ScoreboardScoreUtil;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.scoreboard.Scoreboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Scoreboard.class)
public abstract class ScoreboardExtender implements ScoreboardAccess {
    @Shadow
    protected abstract NbtList toNbt();

    @Shadow
    protected abstract void readNbt(NbtList list);

    @Unique
    private RegistryWrapper.WrapperLookup lookup;

    @Inject(
        method = "toNbt",
        at = @At("HEAD")
    )
    private void toNbtSetLookup(CallbackInfoReturnable<NbtList> info) {
        ScoreboardScoreUtil.setLookup(this.lookup);
    }

    @Inject(
        method = "toNbt",
        at = @At("TAIL")
    )
    private void toNbtResetLookup(CallbackInfoReturnable<NbtList> info) {
        ScoreboardScoreUtil.setLookup(null);
    }

    @Inject(
        method = "readNbt",
        at = @At("HEAD")
    )
    private void readNbtSetLookup(NbtList list, CallbackInfo info) {
        ScoreboardScoreUtil.setLookup(this.lookup);
    }

    @Inject(
        method = "readNbt",
        at = @At("TAIL")
    )
    private void readNbtResetLookup(NbtList list, CallbackInfo info) {
        ScoreboardScoreUtil.setLookup(null);
    }

    @Override
    public NbtList itematic$toNbt(RegistryWrapper.WrapperLookup lookup) {
        this.lookup = lookup;
        NbtList nbt = this.toNbt();
        this.lookup = null;
        return nbt;
    }

    @Override
    public void itematic$readNbt(NbtList nbt, RegistryWrapper.WrapperLookup lookup) {
        this.lookup = lookup;
        this.readNbt(nbt);
        this.lookup = null;
    }
}
