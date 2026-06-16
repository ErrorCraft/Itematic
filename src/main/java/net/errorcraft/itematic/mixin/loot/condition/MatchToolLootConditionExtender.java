package net.errorcraft.itematic.mixin.loot.condition;

import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.MatchToolLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.item.ItemPredicate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MatchToolLootCondition.class)
public class MatchToolLootConditionExtender {
    @Redirect(
        method = "test(Lnet/minecraft/loot/context/LootContext;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/predicate/item/ItemPredicate;test(Lnet/minecraft/item/ItemStack;)Z")
    )
    private boolean passRandom(ItemPredicate instance, ItemStack stack, LootContext lootContext) {
        return instance.itematic$test(stack, lootContext.getRandom());
    }
}
