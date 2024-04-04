package net.errorcraft.itematic.mixin.loot.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.condition.LootConditionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootConditionTypes.class)
public interface LootConditionTypesAccessor {
    @Invoker("register")
    static LootConditionType register(String id, MapCodec<? extends LootCondition> mapCodec) {
        throw new AssertionError();
    }
}
