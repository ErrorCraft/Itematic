package net.errorcraft.itematic.mixin.stat;

import net.errorcraft.itematic.access.scoreboard.ScoreboardCriterionAccess;
import net.errorcraft.itematic.access.stat.StatAccess;
import net.errorcraft.itematic.stat.StatUtil;
import net.minecraft.core.Holder;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Stat.class)
public class StatExtender<T> implements StatAccess<T> {
    @Shadow
    @Final
    private StatType<T> type;

    @Unique
    private Holder<T> entry;

    @Inject(
        method = "buildName(Lnet/minecraft/stats/StatType;Ljava/lang/Object;)Ljava/lang/String;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static <T> void checkNull(StatType<T> type, T value, CallbackInfoReturnable<String> info) {
        if (value == null) {
            info.setReturnValue("");
        }
    }

    @Override
    public Holder<T> itematic$entry() {
        return this.entry;
    }

    @Override
    public void itematic$setEntry(Holder<T> entry) {
        this.entry = entry;
        ((ScoreboardCriterionAccess) this).itematic$setName(StatUtil.statName(this.type, entry));
    }
}
