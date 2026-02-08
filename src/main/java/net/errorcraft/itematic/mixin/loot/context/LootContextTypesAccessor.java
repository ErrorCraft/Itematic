package net.errorcraft.itematic.mixin.loot.context;

import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.util.context.ContextType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(LootContextTypes.class)
public interface LootContextTypesAccessor {
    @Invoker("register")
    static ContextType register(String name, Consumer<ContextType.Builder> type) {
        throw new AssertionError();
    }
}
