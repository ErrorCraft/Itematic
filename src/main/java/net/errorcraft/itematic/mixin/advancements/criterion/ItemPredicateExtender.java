package net.errorcraft.itematic.mixin.advancements.criterion;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.advancements.criterion.ItemPredicateAccess;
import net.errorcraft.itematic.core.registries.ItematicBuiltInRegistries;
import net.errorcraft.itematic.util.SetCodec;
import net.errorcraft.itematic.world.item.behavior.ItemBehaviorType;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Mixin(ItemPredicate.class)
public class ItemPredicateExtender implements ItemPredicateAccess {
    @Unique
    private Optional<Set<ItemBehaviorType<?>>> behavior = Optional.empty();

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
        )
    )
    private static Codec<ItemPredicate> addExtraMapCodecFields(Codec<ItemPredicate> original) {
        return RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.assumeMapUnsafe(original).forGetter(Function.identity()),
            SetCodec.forRegistry(ItematicBuiltInRegistries.ITEM_BEHAVIOR_TYPE).optionalFieldOf("behavior").forGetter(ItemPredicate::itematic$behavior)
        ).apply(instance, ItemPredicateExtender::setBehavior));
    }

    @ModifyReturnValue(
        method = "test(Lnet/minecraft/world/item/ItemStack;)Z",
        at = @At("TAIL")
    )
    private boolean testBehavior(boolean original, ItemStack itemStack) {
        if (!original) {
            return false;
        }

        if (this.behavior.isEmpty()) {
            return true;
        }

        for (ItemBehaviorType<?> type : this.behavior.get()) {
            if (!itemStack.itematic$hasBehavior(type)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Optional<Set<ItemBehaviorType<?>>> itematic$behavior() {
        return this.behavior;
    }

    @Override
    public void itematic$setBehavior(Optional<Set<ItemBehaviorType<?>>> behavior) {
        this.behavior = behavior;
    }

    @Unique
    private static ItemPredicate setBehavior(ItemPredicate predicate, Optional<Set<ItemBehaviorType<?>>> behavior) {
        predicate.itematic$setBehavior(behavior);
        return predicate;
    }

    @Mixin(ItemPredicate.Builder.class)
    public static class BuilderExtender implements ItemPredicateAccess.BuilderAccess {
        @Shadow
        private Optional<HolderSet<Item>> items;

        @Unique
        private final Set<ItemBehaviorType<?>> behavior = new HashSet<>();

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        private ItemPredicate setBehavior(ItemPredicate original) {
            original.itematic$setBehavior(this.behavior.isEmpty() ? Optional.empty() : Optional.of(this.behavior));
            return original;
        }

        @Override
        public ItemPredicate.Builder itematic$items(HolderSet<Item> items) {
            this.items = Optional.of(items);
            return (ItemPredicate.Builder)(Object) this;
        }

        @Override
        public ItemPredicate.Builder itematic$behavior(ItemBehaviorType<?>... behavior) {
            this.behavior.addAll(List.of(behavior));
            return (ItemPredicate.Builder)(Object) this;
        }
    }
}
