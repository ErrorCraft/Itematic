package net.errorcraft.itematic.mixin.loot.function;

import com.mojang.serialization.Codec;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootFunctionTypes.class)
public interface LootFunctionTypesAccessor {
    @Invoker("register")
    static LootFunctionType register(String id, Codec<? extends LootFunction> codec) {
        throw new AssertionError();
    }
}
