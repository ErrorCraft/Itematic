package net.errorcraft.itematic.mixin.stats;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.errorcraft.itematic.access.stats.StatAccess;
import net.errorcraft.itematic.stats.ItematicStats;
import net.minecraft.core.Holder;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Stat.class)
public abstract class StatExtender<T> extends ObjectiveCriteria implements StatAccess<T> {
    @Shadow
    @Final
    private StatType<T> type;

    @Unique
    private Holder<T> entry;

    protected StatExtender(String string) {
        super(string);
    }

    @WrapMethod(
        method = "buildName(Lnet/minecraft/stats/StatType;Ljava/lang/Object;)Ljava/lang/String;"
    )
    private static <T> String checkNull(StatType<T> type, @Nullable T value, Operation<String> original) {
        if (value == null) {
            return "";
        }

        return original.call(type, value);
    }

    @Override
    public Holder<T> itematic$entry() {
        return this.entry;
    }

    @Override
    public void itematic$setEntry(Holder<T> entry) {
        this.entry = entry;
        this.itematic$setName(ItematicStats.statName(this.type, entry));
    }
}
