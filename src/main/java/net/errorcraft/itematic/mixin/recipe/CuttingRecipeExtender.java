package net.errorcraft.itematic.mixin.recipe;

import com.mojang.serialization.Codec;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.registry.DefaultedRegistry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryFixedCodec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CuttingRecipeExtender {
    @Mixin(CuttingRecipe.Serializer.class)
    public static class SerializerExtender<T extends CuttingRecipe> {
        @Redirect(
            method = "method_53880",
            at = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/registry/DefaultedRegistry;getCodec()Lcom/mojang/serialization/Codec;"
            )
        )
        private static Codec<RegistryEntry<Item>> getCodecUseRegistryFixedCodec(DefaultedRegistry<Item> instance) {
            return RegistryFixedCodec.of(RegistryKeys.ITEM);
        }

        @ModifyArg(
            method = "method_53880",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/serialization/MapCodec;forGetter(Ljava/util/function/Function;)Lcom/mojang/serialization/codecs/RecordCodecBuilder;",
                ordinal = 0,
                remap = false
            )
        )
        private static <O, A> Function<ItemStack, RegistryEntry<Item>> forGetterUseRegistryEntry(Function<O, A> getter) {
            return ItemStack::getRegistryEntry;
        }

        @ModifyArg(
            method = "method_53880",
            at = @At(
                value = "INVOKE",
                target = "Lcom/mojang/datafixers/Products$P2;apply(Lcom/mojang/datafixers/kinds/Applicative;Ljava/util/function/BiFunction;)Lcom/mojang/datafixers/kinds/App;",
                remap = false
            )
        )
        private static <T1, T2, R> BiFunction<RegistryEntry<Item>, Integer, ItemStack> applyUseRegistryEntryItemStackConstructor(BiFunction<T1, T2, R> function) {
            return ItemStack::new;
        }
    }
}
