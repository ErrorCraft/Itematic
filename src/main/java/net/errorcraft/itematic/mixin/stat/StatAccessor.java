package net.errorcraft.itematic.mixin.stat;

import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.StatType;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Stat.class)
public interface StatAccessor {
    @Invoker("<init>")
    static <T> Stat<T> create(StatType<T> type, T value, StatFormatter formatter) {
        throw new AssertionError();
    }

    @Invoker("getName")
    static String getName(Identifier id) {
        throw new AssertionError();
    }
}
