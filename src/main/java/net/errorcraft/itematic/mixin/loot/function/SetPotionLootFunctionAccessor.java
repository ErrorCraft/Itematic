package net.errorcraft.itematic.mixin.loot.function;

import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.function.SetPotionLootFunction;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(SetPotionLootFunction.class)
public interface SetPotionLootFunctionAccessor {
    @Invoker("<init>")
    static SetPotionLootFunction create(List<LootCondition> conditions, RegistryEntry<Potion> potion) {
        throw new AssertionError();
    }
}
