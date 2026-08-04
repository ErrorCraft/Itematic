package net.errorcraft.itematic.mixin.loot.function;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootItemFunctions.class)
public interface LootFunctionTypesAccessor {
    @Invoker("register")
    static <T extends LootItemFunction> LootItemFunctionType<T> register(String id, MapCodec<T> codec) {
        throw new AssertionError();
    }
}
