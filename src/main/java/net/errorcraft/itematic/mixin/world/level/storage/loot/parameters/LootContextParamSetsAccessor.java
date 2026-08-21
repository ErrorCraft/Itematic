package net.errorcraft.itematic.mixin.world.level.storage.loot.parameters;

import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(LootContextParamSets.class)
public interface LootContextParamSetsAccessor {
    @Invoker("register")
    static ContextKeySet register(String name, Consumer<ContextKeySet.Builder> type) {
        throw new AssertionError();
    }
}
