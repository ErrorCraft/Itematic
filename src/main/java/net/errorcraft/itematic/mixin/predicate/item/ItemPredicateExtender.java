package net.errorcraft.itematic.mixin.predicate.item;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.predicate.item.ItemPredicateAccess;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.predicate.item.ItemPredicates;
import net.errorcraft.itematic.registry.ItematicRegistries;
import net.errorcraft.itematic.serialization.SetCodec;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.entry.RegistryEntryList;
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
    private Optional<Set<ItemComponentType<?>>> behavior;

    @ModifyExpressionValue(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Codec<ItemPredicate> addExtraMapCodecFields(Codec<ItemPredicate> original) {
        return RecordCodecBuilder.create(instance -> instance.group(
            MapCodec.assumeMapUnsafe(original).forGetter(Function.identity()),
            SetCodec.forRegistry(ItematicRegistries.ITEM_COMPONENT_TYPE).optionalFieldOf("behavior").forGetter(ItemPredicate::itematic$behavior)
        ).apply(instance, ItemPredicates::setBehavior));
    }

    @ModifyReturnValue(
        method = "test(Lnet/minecraft/item/ItemStack;)Z",
        at = @At("TAIL")
    )
    private boolean testBehavior(boolean original, ItemStack stack) {
        if (this.behavior.isEmpty()) {
            return true;
        }

        for (ItemComponentType<?> type : this.behavior.get()) {
            if (!stack.itematic$hasBehavior(type)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Optional<Set<ItemComponentType<?>>> itematic$behavior() {
        return this.behavior;
    }

    @Override
    public void itematic$setBehavior(Optional<Set<ItemComponentType<?>>> behavior) {
        this.behavior = behavior;
    }

    @Mixin(ItemPredicate.Builder.class)
    public static class BuilderExtender implements ItemPredicateAccess.BuilderAccess {
        @Shadow
        private Optional<RegistryEntryList<Item>> item;

        @Unique
        private final Set<ItemComponentType<?>> behavior = new HashSet<>();

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        private ItemPredicate setBehavior(ItemPredicate original) {
            original.itematic$setBehavior(this.behavior.isEmpty() ? Optional.empty() : Optional.of(this.behavior));
            return original;
        }

        @Override
        public ItemPredicate.Builder itematic$items(RegistryEntryList<Item> items) {
            this.item = Optional.of(items);
            return (ItemPredicate.Builder)(Object) this;
        }

        @Override
        public ItemPredicate.Builder itematic$behavior(ItemComponentType<?>... behavior) {
            this.behavior.addAll(List.of(behavior));
            return (ItemPredicate.Builder)(Object) this;
        }
    }
}
