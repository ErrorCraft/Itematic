package net.errorcraft.itematic.mixin.stat;

import net.errorcraft.itematic.access.scoreboard.ScoreboardCriterionAccess;
import net.errorcraft.itematic.access.stat.StatAccess;
import net.errorcraft.itematic.stat.StatUtil;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatType;
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
    private RegistryEntry<T> entry;

    @Inject(
        method = "getName(Lnet/minecraft/stat/StatType;Ljava/lang/Object;)Ljava/lang/String;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static <T> void checkNull(StatType<T> type, T value, CallbackInfoReturnable<String> info) {
        if (value == null) {
            info.setReturnValue("");
        }
    }

    @Override
    public RegistryEntry<T> itematic$entry() {
        return this.entry;
    }

    @Override
    public void itematic$setEntry(RegistryEntry<T> entry) {
        this.entry = entry;
        ((ScoreboardCriterionAccess) this).itematic$setName(StatUtil.statName(this.type, entry));
    }
}
