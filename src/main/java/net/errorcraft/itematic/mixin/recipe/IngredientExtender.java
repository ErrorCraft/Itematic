package net.errorcraft.itematic.mixin.recipe;

import net.errorcraft.itematic.access.recipe.IngredientAccess;
import net.errorcraft.itematic.access.recipe.IngredientEntryAccess;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(Ingredient.class)
public class IngredientExtender implements IngredientAccess {
    @Shadow
    @Final
    private Ingredient.Entry[] entries;

    @Shadow
    @Nullable
    private ItemStack[] matchingStacks;

    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/codec/PacketCodec;xmap(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/PacketCodec;"
        )
    )
    private static PacketCodec<RegistryByteBuf, Ingredient> xmapUseDynamicRegistry(PacketCodec<RegistryByteBuf, List<ItemStack>> instance, Function<? super List<ItemStack>, ? extends Ingredient> _to, Function<? super Ingredient, ? extends List<ItemStack>> from) {
        return PacketCodec.ofStatic(
            (buf, ingredient) -> instance.encode(buf, List.of(ingredient.itematic$getMatchingStacks(buf.getRegistryManager()))),
            buf -> _to.apply(instance.decode(buf))
        );
    }

    @Redirect(
        method = "ofItems",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Arrays;stream([Ljava/lang/Object;)Ljava/util/stream/Stream;"
        )
    )
    private static <T> Stream<T> streamItemsReturnEmptyStream(T[] array) {
        return Stream.empty();
    }

    @Inject(
        method = "test(Lnet/minecraft/item/ItemStack;)Z",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/recipe/Ingredient;getMatchingStacks()[Lnet/minecraft/item/ItemStack;"
        ),
        cancellable = true
    )
    private void testUseDirectEntries(ItemStack itemStack, CallbackInfoReturnable<Boolean> info) {
        for (Ingredient.Entry entry : this.entries) {
            if (entry.itematic$test(itemStack)) {
                info.setReturnValue(true);
                return;
            }
        }
        info.setReturnValue(false);
    }

    @Override
    public ItemStack[] itematic$getMatchingStacks(RegistryWrapper.WrapperLookup lookup) {
        if (this.matchingStacks == null) {
            this.matchingStacks = Arrays.stream(this.entries)
                .flatMap(entry -> entry.itematic$getStacks(lookup).stream())
                .distinct()
                .toArray(ItemStack[]::new);
        }
        return this.matchingStacks;
    }

    @Mixin(targets = "net/minecraft/recipe/Ingredient$TagEntry")
    public static class TagEntryExtender implements IngredientEntryAccess {
        @Shadow
        @Final
        private TagKey<Item> tag;

        @Override
        public boolean itematic$test(ItemStack stack) {
            return stack.isIn(this.tag);
        }

        @Override
        public Collection<ItemStack> itematic$getStacks(RegistryWrapper.WrapperLookup lookup) {
            ArrayList<ItemStack> stacks = new ArrayList<>();
            lookup.getWrapperOrThrow(RegistryKeys.ITEM)
                .getOptional(this.tag)
                .ifPresent(entries -> entries.forEach(entry -> stacks.add(new ItemStack(entry))));
            return stacks;
        }
    }

    @Mixin(targets = "net/minecraft/recipe/Ingredient$StackEntry")
    public static abstract class StackEntryExtender implements IngredientEntryAccess {
        @Shadow
        @Final
        private ItemStack stack;

        @Shadow
        public abstract Collection<ItemStack> getStacks();

        @Override
        public boolean itematic$test(ItemStack stack) {
            return stack.itemMatches(this.stack.getRegistryEntry());
        }

        @Override
        public Collection<ItemStack> itematic$getStacks(RegistryWrapper.WrapperLookup lookup) {
            return this.getStacks();
        }
    }
}
