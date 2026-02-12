package net.errorcraft.itematic.mixin.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

@Mixin(Ingredient.class)
public class IngredientExtender implements IngredientAccess {
    @Shadow
    @Final
    @Mutable
    public static Codec<Ingredient> ALLOW_EMPTY_CODEC;

    @Shadow
    @Final
    @Mutable
    public static Codec<Ingredient> DISALLOW_EMPTY_CODEC;

    @Shadow
    @Final
    private Ingredient.Entry[] entries;

    @Shadow
    @Nullable
    private ItemStack[] matchingStacks;

    @Unique
    private Optional<ItemStack> remainder = Optional.empty();

    static {
        // Modify the codec fields in a static initializer block due to Fabric API using a cancellable @Inject for their own additions, which makes @ModifyReturnValue not work
        ALLOW_EMPTY_CODEC = addRemainder(ALLOW_EMPTY_CODEC);
        DISALLOW_EMPTY_CODEC = addRemainder(DISALLOW_EMPTY_CODEC);
    }

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

    @Override
    public Optional<ItemStack> itematic$remainder() {
        return this.remainder;
    }

    @Override
    public void itematic$setRemainder(Optional<ItemStack> remainder) {
        this.remainder = remainder;
    }

    @Unique
    private static Codec<Ingredient> addRemainder(Codec<Ingredient> original) {
        Codec<Ingredient> fullCodec = RecordCodecBuilder.create(instance -> instance.group(
            original.fieldOf("items").forGetter(Function.identity()),
            ItemStack.CODEC.optionalFieldOf("remainder").forGetter(Ingredient::itematic$remainder)
        ).apply(instance, IngredientExtender::setFields));
        return Codec.either(original, fullCodec)
            .xmap(
                either -> either.map(Function.identity(), Function.identity()),
                ingredient -> {
                    if (ingredient.itematic$remainder().isEmpty()) {
                        return Either.left(ingredient);
                    }

                    return Either.right(ingredient);
                }
            );
    }

    @Unique
    private static Ingredient setFields(Ingredient ingredient, Optional<ItemStack> remainder) {
        ingredient.itematic$setRemainder(remainder);
        return ingredient;
    }

    @Mixin(Ingredient.Entry.class)
    public interface EntryExtender extends IngredientEntryAccess {}

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
