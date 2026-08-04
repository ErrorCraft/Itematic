package net.errorcraft.itematic.mixin.loot.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootItemConditions.class)
public interface LootConditionTypesAccessor {
    @Invoker("register")
    static LootItemConditionType register(String id, MapCodec<? extends LootItemCondition> mapCodec) {
        throw new AssertionError();
    }
}
