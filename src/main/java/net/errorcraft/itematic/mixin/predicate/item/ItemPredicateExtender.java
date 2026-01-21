package net.errorcraft.itematic.mixin.predicate.item;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.errorcraft.itematic.access.predicate.item.ItemPredicateAccess;
import net.errorcraft.itematic.access.predicate.item.ItemPredicateBuilderAccess;
import net.errorcraft.itematic.item.component.ItemComponentType;
import net.errorcraft.itematic.predicate.item.ItemPredicateExtraFields;
import net.minecraft.component.ComponentType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.registry.entry.RegistryEntryList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Mixin(ItemPredicate.class)
public class ItemPredicateExtender implements ItemPredicateAccess {
    @Unique
    private ItemPredicateExtraFields extraFields;

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
            remap = false
        )
    )
    private static Codec<ItemPredicate> createCodecAddExtraFields(Function<RecordCodecBuilder.Instance<ItemPredicate>, ? extends App<RecordCodecBuilder.Mu<ItemPredicate>, ItemPredicate>> builder) {
        return RecordCodecBuilder.mapCodec(builder).dependent(
            ItemPredicateExtraFields.CODEC,
            itemPredicate -> Pair.of(
                ((ItemPredicateExtender)(Object) itemPredicate).extraFields,
                ItemPredicateExtraFields.CODEC
            ),
            (itemPredicate, extraFields) -> {
                ((ItemPredicateExtender)(Object) itemPredicate).extraFields = extraFields;
                return itemPredicate;
            })
            .codec();
    }

    @ModifyReturnValue(
        method = "test(Lnet/minecraft/item/ItemStack;)Z",
        at = @At("TAIL")
    )
    private boolean testExtraFields(boolean original, ItemStack stack) {
        return this.extraFields.testExtraFields(stack);
    }

    @Override
    public ItemPredicateExtraFields itematic$extraFields() {
        return this.extraFields;
    }

    @Override
    public void itematic$setExtraFields(ItemPredicateExtraFields extraFields) {
        this.extraFields = extraFields;
    }

    @Mixin(ItemPredicate.Builder.class)
    public static class BuilderExtender implements ItemPredicateBuilderAccess {
        @Shadow
        private Optional<RegistryEntryList<Item>> item;

        @Unique
        private final Set<ItemComponentType<?>> behavior = new HashSet<>();

        @Unique
        private final Set<ComponentType<?>> dataComponents = new HashSet<>();

        @ModifyReturnValue(
            method = "build",
            at = @At("TAIL")
        )
        private ItemPredicate setExtraFields(ItemPredicate original) {
            ((ItemPredicateAccess)(Object) original).itematic$setExtraFields(ItemPredicateExtraFields.of(this.behavior, this.dataComponents));
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

        @Override
        public ItemPredicate.Builder itematic$dataComponents(ComponentType<?>... dataComponents) {
            this.dataComponents.addAll(List.of(dataComponents));
            return (ItemPredicate.Builder)(Object) this;
        }
    }
}
