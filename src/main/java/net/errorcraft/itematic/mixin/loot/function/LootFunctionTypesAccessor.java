package net.errorcraft.itematic.mixin.loot.function;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.function.LootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.function.LootFunctionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootFunctionTypes.class)
public interface LootFunctionTypesAccessor {
    @Invoker("register")
    static <T extends LootFunction> LootFunctionType<T> register(String id, MapCodec<T> codec) {
        throw new AssertionError();
    }
}
