package net.errorcraft.itematic.mixin.loot.context;

import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootContextParameters.class)
public interface LootContextParametersAccessor {
    @Invoker("register")
    static <T> LootContextParameter<T> register(String name) {
        throw new AssertionError();
    }
}
