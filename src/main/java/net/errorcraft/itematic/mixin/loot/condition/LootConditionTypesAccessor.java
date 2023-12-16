package net.errorcraft.itematic.mixin.loot.condition;

import com.mojang.serialization.Codec;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.condition.LootConditionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LootConditionTypes.class)
public interface LootConditionTypesAccessor {
    @Invoker("register")
    static LootConditionType register(String id, Codec<? extends LootCondition> codec) {
        throw new AssertionError();
    }
}
