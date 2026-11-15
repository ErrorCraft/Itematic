package net.errorcraft.itematic.mixin.advancements.predicates;

import net.errorcraft.itematic.access.advancements.predicates.ItemPredicateAccess;
import net.minecraft.advancements.predicates.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

public class ItemPredicateExtender {
    @Mixin(ItemPredicate.Builder.class)
    public static class BuilderExtender implements ItemPredicateAccess.BuilderAccess {
        @Shadow
        private Optional<HolderSet<Item>> items;

        @Override
        public ItemPredicate.Builder itematic$items(HolderSet<Item> items) {
            this.items = Optional.of(items);
            return (ItemPredicate.Builder)(Object) this;
        }
    }
}
